package ru.naumen.calorietracker.dto;

import java.math.BigDecimal;

public record AddFoodEntryRequest(Integer productId, Integer mealId, BigDecimal portionGrams) {}
