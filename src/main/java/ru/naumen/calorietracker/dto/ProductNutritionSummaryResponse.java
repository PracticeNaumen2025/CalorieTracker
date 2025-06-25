package ru.naumen.calorietracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductNutritionSummaryResponse {
        private Integer productId;
        private String productName;
        private BigDecimal totalCalories;
        private BigDecimal totalProtein;
        private BigDecimal totalFat;
        private BigDecimal totalCarb;
}
