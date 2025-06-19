package ru.naumen.calorietracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DaySummaryDTO(
        LocalDate date,
        BigDecimal totalCalories,
        BigDecimal totalProtein,
        BigDecimal totalFat,
        BigDecimal totalCarbs
) {}
