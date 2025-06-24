package ru.naumen.calorietracker.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UserResponse(
    Integer userId,
    String username,
    String photoUrl,
    LocalDate dateOfBirth,
    String gender,
    BigDecimal heightCm,
    BigDecimal weightKg,
    String activityLevel
) implements Serializable {}
