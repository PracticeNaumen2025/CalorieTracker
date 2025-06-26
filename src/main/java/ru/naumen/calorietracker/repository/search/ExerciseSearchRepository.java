package ru.naumen.calorietracker.repository.search;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ru.naumen.calorietracker.model.elastic.ExerciseSearchDocument;
import java.util.List;

@Repository
public interface ExerciseSearchRepository extends ElasticsearchRepository<ExerciseSearchDocument, Integer> {
    @Query("{\"match\": {\"name\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    List<ExerciseSearchDocument> findByNameFuzzy(String name);

    @Query("{\"match_phrase_prefix\": {\"name\": \"?0\"}}")
    List<ExerciseSearchDocument> findByNamePrefix(String name);
} 