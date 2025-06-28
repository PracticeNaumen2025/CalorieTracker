package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сущность, описывающая продукт с его пищевой ценностью на 100 грамм.
 * Связан с категорией и пользователем, который его добавил.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    /**
     * Уникальный идентификатор продукта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    /**
     * Название продукта.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Калорийность продукта на 100 грамм.
     */
    @Column(name = "calories_per_100g", nullable = false)
    private BigDecimal caloriesPer100g;

    /**
     * Содержание белка на 100 грамм.
     */
    @Column(name = "protein_per_100g", nullable = false)
    private BigDecimal proteinPer100g;

    /**
     * Содержание жиров на 100 грамм.
     */
    @Column(name = "fat_per_100g", nullable = false)
    private BigDecimal fatPer100g;

    /**
     * Содержание углеводов на 100 грамм.
     */
    @Column(name = "carbs_per_100g", nullable = false)
    private BigDecimal carbsPer100g;

    /**
     * Категория, к которой относится продукт.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * Пользователь, добавивший продукт.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User user;

    /**
     * Флаг мягкого удаления продукта.
     */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    /**
     * Дата и время создания записи.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Дата и время последнего обновления записи.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Устанавливает дату создания перед сохранением.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Обновляет дату обновления перед изменением.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
