package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "foodentries")
@Getter
@Setter
public class FoodEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Integer entryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "portion_grams", nullable = false)
    private BigDecimal portionGrams;

    @Column(name = "calories", nullable = false)
    private BigDecimal calories;

    @Column(name = "protein_g", nullable = false)
    private BigDecimal proteinG;

    @Column(name = "fat_g", nullable = false)
    private BigDecimal fatG;

    @Column(name = "carbs_g", nullable = false)
    private BigDecimal carbsG;
}