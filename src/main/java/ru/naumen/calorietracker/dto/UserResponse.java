package ru.naumen.calorietracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserResponse(
    Integer userId,
    String username,
    LocalDate dateOfBirth,
    String gender,
    BigDecimal heightCm,
    BigDecimal weightKg,
    String activityLevel){}
