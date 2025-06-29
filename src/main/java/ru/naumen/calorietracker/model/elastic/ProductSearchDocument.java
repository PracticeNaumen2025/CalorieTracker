package ru.naumen.calorietracker.model.elastic;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * Документ продукта для индекса Elasticsearch.
 * Используется для поиска продуктов по названию и фильтрации по нутриентам.
 */
@Document(indexName = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDocument {

    /**
     * Уникальный идентификатор продукта.
     */
    @Id
    @Field(name = "product_id", type = FieldType.Integer)
    private Integer productId;

    /**
     * Название продукта (поддерживает полнотекстовый поиск).
     */
    @Field(name = "name", type = FieldType.Text, analyzer = "standard")
    private String name;

    /**
     * Калорийность на 100 г продукта.
     */
    @Field(name = "calories_per_100g", type = FieldType.Double)
    private BigDecimal caloriesPer100g;

    /**
     * Белки на 100 г продукта.
     */
    @Field(name = "protein_per_100g", type = FieldType.Double)
    private BigDecimal proteinPer100g;

    /**
     * Жиры на 100 г продукта.
     */
    @Field(name = "fat_per_100g", type = FieldType.Double)
    private BigDecimal fatPer100g;

    /**
     * Углеводы на 100 г продукта.
     */
    @Field(name = "carbs_per_100g", type = FieldType.Double)
    private BigDecimal carbsPer100g;

    /**
     * Название категории, к которой принадлежит продукт.
     */
    @Field(name = "category_name", type = FieldType.Keyword)
    private String categoryName;

    /**
     * Идентификатор пользователя, добавившего продукт.
     */
    @Field(name = "user_id", type = FieldType.Integer)
    private Integer userId;
}
