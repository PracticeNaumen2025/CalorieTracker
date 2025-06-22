package ru.naumen.calorietracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FoodEntryDTO {
    private Integer entryId;
    private Integer mealId;
    private Integer productId;
    private BigDecimal portionGrams;
    private BigDecimal calories;
    private BigDecimal proteinG;
    private BigDecimal fatG;
    private BigDecimal carbsG;
}
