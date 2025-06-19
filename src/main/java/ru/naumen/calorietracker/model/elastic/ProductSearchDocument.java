package ru.naumen.calorietracker.model.elastic;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Document(indexName = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDocument {

    @Id
    @Field(name = "product_id", type = FieldType.Integer)
    private Integer productId;

    @Field(name = "name", type = FieldType.Text, analyzer = "standard")
    private String name;

    @Field(name = "calories_per_100g", type = FieldType.Double)
    private BigDecimal caloriesPer100g;

    @Field(name = "protein_per_100g", type = FieldType.Double)
    private BigDecimal proteinPer100g;

    @Field(name = "fat_per_100g", type = FieldType.Double)
    private BigDecimal fatPer100g;

    @Field(name = "carbs_per_100g", type = FieldType.Double)
    private BigDecimal carbsPer100g;

    @Field(name = "category_name", type = FieldType.Keyword)
    private String categoryName;

    @Field(name = "user_id", type = FieldType.Integer)
    private Integer userId;
}
