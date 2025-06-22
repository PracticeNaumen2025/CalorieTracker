package ru.naumen.calorietracker.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MealWithEntriesResponse(
        Integer mealId,
        LocalDateTime dateTime,
        String mealType,
        List<FoodEntryWithProductResponse> foodEntries
) {}
