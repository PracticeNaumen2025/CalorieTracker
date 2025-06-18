package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Integer id);
}
