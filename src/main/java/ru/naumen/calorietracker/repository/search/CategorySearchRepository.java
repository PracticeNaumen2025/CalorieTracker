package ru.naumen.calorietracker.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.naumen.calorietracker.model.elastic.CategorySearchDocument;


public interface CategorySearchRepository extends ElasticsearchRepository<CategorySearchDocument, Integer> {
    Page<CategorySearchDocument> findByCategoryNameStartingWith(String categoryName, Pageable pageable);
}
