package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.ProductCreateRequest;
import ru.naumen.calorietracker.dto.ProductResponse;
import ru.naumen.calorietracker.dto.ProductUpdateRequest;
import ru.naumen.calorietracker.model.User;

import java.util.List;

/**
 * Сервис для работы с продуктами.
 */
public interface ProductService {

    /**
     * Возвращает список всех продуктов.
     * @return Список объектов ProductResponse.
     */
    List<ProductResponse> getAllProducts();
    /**
     * Возвращает продукт по его идентификатору.
     * @param id Идентификатор продукта.
     * @return Объект ProductResponse, представляющий продукт.
     * @throws RuntimeException Если продукт не найден или удален.
     */
    ProductResponse getProductById(Integer id) throws RuntimeException;
    /**
     * Создает новый продукт.
     * @param request Запрос на создание продукта.
     * @param user Пользователь, создающий продукт.
     * @return Объект ProductResponse, представляющий созданный продукт.
     */
    ProductResponse createProduct(ProductCreateRequest request, User user);
    /**
     * Обновляет существующий продукт.
     * @param id Идентификатор продукта для обновления.
     * @param request Запрос на обновление продукта.
     * @param user Пользователь, обновляющий продукт.
     * @return Объект ProductResponse, представляющий обновленный продукт.
     * @throws RuntimeException Если продукт не найден, удален или у пользователя нет прав на редактирование.
     */
    ProductResponse updateProduct(Integer id, ProductUpdateRequest request, User user) throws RuntimeException;
    /**
     * Удаляет продукт по его идентификатору (помечает как удаленный).
     * @param id Идентификатор продукта для удаления.
     * @throws RuntimeException Если продукт не найден или уже удален.
     */
    void deleteProduct(Integer id) throws RuntimeException;
    /**
     * Ищет продукты по имени.
     * @param name Часть имени продукта для поиска.
     * @return Список объектов ProductResponse, соответствующих критериям поиска.
     */
    List<ProductResponse> searchByName(String name);
}
