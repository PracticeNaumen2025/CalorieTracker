package ru.naumen.calorietracker.dto;
import java.time.LocalDate;

public record DaySummaryResponse(
        LocalDate date,
        Double totalCalories,
        Double totalProtein,
        Double totalFat,
        Double totalCarbs
) {}
