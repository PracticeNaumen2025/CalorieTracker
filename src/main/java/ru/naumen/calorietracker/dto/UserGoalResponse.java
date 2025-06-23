package ru.naumen.calorietracker.dto;

import java.io.Serializable;
import java.time.LocalDate;

public record UserGoalResponse(
        int goalId,
        LocalDate startDate,
        LocalDate endDate,
        double targetWeightKg,
        int dailyCalorieGoal,
        double proteinPercentage,
        double fatPercentage,
        double carbPercentage
) implements Serializable { }
