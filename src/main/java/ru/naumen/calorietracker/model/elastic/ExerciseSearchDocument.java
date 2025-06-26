package ru.naumen.calorietracker.model.elastic;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Document(indexName = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSearchDocument {
    @Id
    @Field(name = "exercise_id", type = FieldType.Integer)
    private Integer exerciseId;

    @Field(name = "name", type = FieldType.Text, analyzer = "standard")
    private String name;

    @Field(name = "calories_per_hour", type = FieldType.Double)
    private BigDecimal caloriesPerHour;

    @Field(name = "user_id", type = FieldType.Integer)
    private Integer userId;
} 