package ru.naumen.calorietracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.naumen.calorietracker.dto.CategoryResponse;
import ru.naumen.calorietracker.service.CategoryService;

/**
 * REST контроллер для работы с категориями.
 */
@RestController
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Поиск категорий по префиксу имени с пагинацией.
     *
     * @param name имя или префикс имени категории для поиска
     * @param pageable параметры пагинации (страница, размер)
     * @return страница с категориями, имя которых начинается с заданного префикса
     */
    @GetMapping
    public Page<CategoryResponse> findCategoriesByNamePrefix(
            @RequestParam String name,
            @PageableDefault(page = 0, size = 10)
            Pageable pageable
    ) {
        Pageable noSortPageable = Pageable.ofSize(pageable.getPageSize()).withPage(pageable.getPageNumber());
        return categoryService.searchCategories(name, noSortPageable);
    }
}
