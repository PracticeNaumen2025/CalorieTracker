package ru.naumen.calorietracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExerciseUpdateRequest {

    @PositiveOrZero(message = "Идентификатор не должен быть отрицательным")
    private Integer exerciseId;

    @NotBlank(message = "Название обязательно")
    private String name;

    @NotNull(message = "Калории в час обязательны")
    @Positive(message = "Калории должны быть положительными")
    private BigDecimal caloriesPerHour;
} 