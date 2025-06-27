package ru.naumen.calorietracker.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import ru.naumen.calorietracker.dto.UserRegisterRequest;
import ru.naumen.calorietracker.dto.UserResponse;
import ru.naumen.calorietracker.dto.UserUpdateRequest;

import java.io.IOException;

/**
 * Сервис для управления пользователями и их аутентификацией.
 */
public interface UserService extends UserDetailsService {
    /**
     * Регистрирует нового пользователя в системе.
     * @param request Запрос на регистрацию пользователя.
     * @return Объект UserResponse, представляющий зарегистрированного пользователя.
     */
    UserResponse register(UserRegisterRequest request);

    /**
     * Возвращает информацию о пользователе по его имени пользователя.
     * @param username Имя пользователя.
     * @return Объект UserResponse, представляющий пользователя.
     */
    UserResponse getUserByUsername(String username);

    /**
     * Обновляет профиль пользователя.
     * @param username Имя пользователя, чей профиль обновляется.
     * @param updateRequest Запрос на обновление профиля пользователя.
     * @return Объект UserResponse, представляющий обновленный профиль пользователя.
     */
    UserResponse updateUserProfile(String username, UserUpdateRequest updateRequest);

    /**
     * Обновляет фотографию пользователя.
     * @param username Имя пользователя, чья фотография обновляется.
     * @param file Файл фотографии.
     * @return Объект UserResponse, представляющий пользователя с обновленной фотографией.
     * @throws IOException Если произошла ошибка ввода-вывода при работе с файлом.
     */
    UserResponse updateUserPhoto(String username, MultipartFile file) throws IOException;
}
