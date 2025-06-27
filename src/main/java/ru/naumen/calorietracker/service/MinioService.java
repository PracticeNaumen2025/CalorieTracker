package ru.naumen.calorietracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
/**
 * Сервис для взаимодействия с Minio (S3-совместимым хранилищем) для загрузки и удаления файлов.
 */
public class MinioService {

    private final S3Client s3Client;

    @Value("${minio.bucket.icons}")
    private String iconsBucketName;


    @Value("${minio.external.url}")
    private String minioExternalUrl;

    /**
     * Сохраняет файл-иконку в Minio.
     * @param file Файл для сохранения.
     * @return URL сохраненного файла.
     * @throws IOException Если произошла ошибка ввода-вывода.
     * @throws IllegalArgumentException Если файл пуст, превышает размер 10 МБ или не является изображением (JPEG, PNG).
     */
    public String storeIconFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds 10 MB");
        }

        String contentType = file.getContentType();
        if (!isImage(contentType)) {
            throw new IllegalArgumentException("Only image files (JPEG, PNG) are allowed");
        }

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(iconsBucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        return String.format("%s/%s/%s", minioExternalUrl, iconsBucketName, fileName);
    }

    /**
     * Удаляет файл-иконку из Minio по ее URL.
     * @param fileUrl URL файла для удаления.
     */
    public void deleteIconFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3Client.deleteObject(b -> b.bucket(iconsBucketName).key(fileName));
    }

    /**
     * Проверяет, является ли тип контента изображением (JPEG или PNG).
     * @param contentType Тип контента файла.
     * @return true, если тип контента является изображением, иначе false.
     */
    private boolean isImage(String contentType) {
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/png")
        );
    }

}
