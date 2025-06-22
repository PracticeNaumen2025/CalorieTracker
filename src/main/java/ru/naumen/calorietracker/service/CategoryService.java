package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.CategoryResponse;
import ru.naumen.calorietracker.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Integer id);
    Page<CategoryResponse> searchCategories(String name, Pageable pageable);
}
