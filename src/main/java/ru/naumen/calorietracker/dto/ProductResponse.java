package ru.naumen.calorietracker.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Integer productId,
        String name,
        BigDecimal caloriesPer100g,
        BigDecimal proteinPer100g,
        BigDecimal fatPer100g,
        BigDecimal carbsPer100g,
        Integer categoryId,
        String categoryName
) {}