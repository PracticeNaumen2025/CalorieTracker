package ru.naumen.calorietracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateRequest {
    private LocalDate dateOfBirth;
    private String gender;
    private BigDecimal heightCm;
    private BigDecimal weightKg;
    private String activityLevel;
}
