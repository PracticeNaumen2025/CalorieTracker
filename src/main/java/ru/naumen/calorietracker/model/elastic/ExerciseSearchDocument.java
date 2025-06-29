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
 * Документ упражнения для индекса Elasticsearch.
 * Используется для поиска упражнений по названию и фильтрации по калориям.
 */
@Document(indexName = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSearchDocument {
    /**
     * Уникальный идентификатор упражнения.
     */
    @Id
    @Field(name = "exercise_id", type = FieldType.Integer)
    private Integer exerciseId;

    /**
     * Название упражнения (поддерживает полнотекстовый поиск).
     */
    @Field(name = "name", type = FieldType.Text, analyzer = "standard")
    private String name;

    /**
     * Количество калорий, сжигаемых в час.
     */
    @Field(name = "calories_per_hour", type = FieldType.Double)
    private BigDecimal caloriesPerHour;

    /**
     * Идентификатор пользователя, добавившего упражнение.
     */
    @Field(name = "user_id", type = FieldType.Integer)
    private Integer userId;
} 