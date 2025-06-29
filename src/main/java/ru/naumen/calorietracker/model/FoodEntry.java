package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Сущность, представляющая запись о потреблении конкретного продукта в составе приёма пищи.
 * Содержит информацию о граммах порции и пищевой ценности.
 */
@Entity
@Table(name = "foodentries")
@Getter
@Setter
public class FoodEntry {
    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Integer entryId;

    /**
     * Прием пищи, к которому относится запись.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    /**
     * Продукт, который был потреблен.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Масса потребленного продукта в граммах.
     */
    @Column(name = "portion_grams", nullable = false)
    private BigDecimal portionGrams;

    /**
     * Количество калорий в потребленной порции.
     */
    @Column(name = "calories", nullable = false)
    private BigDecimal calories;

    /**
     * Содержание белка (в граммах) в порции.
     */
    @Column(name = "protein_g", nullable = false)
    private BigDecimal proteinG;

    /**
     * Содержание жиров (в граммах) в порции.
     */
    @Column(name = "fat_g", nullable = false)
    private BigDecimal fatG;

    /**
     * Содержание углеводов (в граммах) в порции.
     */
    @Column(name = "carbs_g", nullable = false)
    private BigDecimal carbsG;
}