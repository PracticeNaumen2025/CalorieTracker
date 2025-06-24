package ru.naumen.calorietracker.model.elastic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategorySearchDocument {
    @Id
    @Field(name = "category_id", type = FieldType.Integer)
    private Integer categoryId;

    @Field(name = "category_name", type = FieldType.Text, analyzer = "standard")
    private String categoryName;
}
