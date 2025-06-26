package ru.naumen.calorietracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExerciseCreateRequest {
    @NotBlank(message = "Название обязательно")
    private String name;

    @NotNull(message = "Калории в час обязательны")
    @Positive(message = "Калории должны быть положительными")
    private BigDecimal caloriesPerHour;
} 