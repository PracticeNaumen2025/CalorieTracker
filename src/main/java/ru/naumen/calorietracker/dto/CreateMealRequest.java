package ru.naumen.calorietracker.dto;

import java.time.LocalDateTime;

public record CreateMealRequest(LocalDateTime dateTime, String mealType) {}
