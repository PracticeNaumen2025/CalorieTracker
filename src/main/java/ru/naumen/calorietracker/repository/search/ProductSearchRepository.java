package ru.naumen.calorietracker.repository.search;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ru.naumen.calorietracker.model.elastic.ProductSearchDocument;
import java.util.List;

@Repository
public interface ProductSearchRepository
        extends ElasticsearchRepository<ProductSearchDocument, String> {

    @Query("{\"match\": {\"name\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    List<ProductSearchDocument> findByNameFuzzy(String name);

    @Query("{\"match_phrase_prefix\": {\"name\": \"?0\"}}")
    List<ProductSearchDocument> findByNamePrefix(String name);
}