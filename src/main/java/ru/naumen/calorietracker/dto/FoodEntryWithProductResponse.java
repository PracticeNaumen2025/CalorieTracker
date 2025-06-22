package ru.naumen.calorietracker.dto;

import java.math.BigDecimal;

public record FoodEntryWithProductResponse(
        Integer entryId,
        String productName,
        BigDecimal portionGrams,
        BigDecimal calories,
        BigDecimal proteinG,
        BigDecimal fatG,
        BigDecimal carbsG
) {}
