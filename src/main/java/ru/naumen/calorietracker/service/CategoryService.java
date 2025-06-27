package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.CategoryResponse;
import ru.naumen.calorietracker.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Сервис для работы с категориями продуктов.
 */
public interface CategoryService {
    /**
     * Возвращает список всех категорий.
     * @return Список объектов Category.
     */
    List<Category> getAllCategories();
    /**
     * Возвращает категорию по ее идентификатору.
     * @param id Идентификатор категории.
     * @return Объект Category, если найден, иначе null.
     */
    Category getCategoryById(Integer id);
    /**
     * Ищет категории по имени с пагинацией.
     * @param name Часть имени категории для поиска.
     * @param pageable Объект Pageable для пагинации.
     * @return Страница объектов CategoryResponse, соответствующих критериям поиска.
     */
    Page<CategoryResponse> searchCategories(String name, Pageable pageable);
}
