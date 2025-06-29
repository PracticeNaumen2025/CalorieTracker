package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;

/**
 * Сущность категории продуктов.
 * Используется для группировки элементов (например, "Овощи", "Фрукты").
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {
    /**
     * Уникальный идентификатор категории.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    /**
     * Уникальное название категории.
     */
    @Column(name = "category_name", nullable = false, unique = true, length = 50)
    private String categoryName;

    /**
     * Дата и время последнего обновления записи.
     * Обновляется автоматически при каждом изменении.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}