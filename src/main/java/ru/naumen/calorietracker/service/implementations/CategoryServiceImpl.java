package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.naumen.calorietracker.dto.CategoryResponse;
import ru.naumen.calorietracker.mapper.CategoryMapper;
import ru.naumen.calorietracker.model.Category;
import ru.naumen.calorietracker.repository.CategoryRepository;
import ru.naumen.calorietracker.repository.search.CategorySearchRepository;
import ru.naumen.calorietracker.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.naumen.calorietracker.model.elastic.CategorySearchDocument;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategorySearchRepository categorySearchRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Page<CategoryResponse> searchCategories(String name, Pageable pageable) {
        Page<CategorySearchDocument> searchResult = categorySearchRepository.findByCategoryNameStartingWith(name, pageable);
        return categoryMapper.toResponsePage(searchResult);
    }
}
