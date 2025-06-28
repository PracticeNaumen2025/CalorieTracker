package ru.naumen.calorietracker.model.elastic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Документ категории для индекса Elasticsearch.
 * Используется для поиска категорий по названию.
 */
@Document(indexName = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategorySearchDocument {

    /**
     * Уникальный идентификатор категории.
     */
    @Id
    @Field(name = "category_id", type = FieldType.Integer)
    private Integer categoryId;

    /**
     * Название категории (поддерживает полнотекстовый поиск).
     */
    @Field(name = "category_name", type = FieldType.Text, analyzer = "standard")
    private String categoryName;
}
