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
public class ProductUpdateRequest {
    @NotBlank(message = "Название обязательно")
    private String name;

    @NotNull(message = "Калорийность обязательна")
    @Positive(message = "Калорийность должна быть положительной")
    private BigDecimal caloriesPer100g;

    @NotNull(message = "Белки обязательны")
    @PositiveOrZero(message = "Белки не могут быть отрицательными")
    private BigDecimal proteinPer100g;

    @NotNull(message = "Жиры обязательны")
    @PositiveOrZero(message = "Жиры не могут быть отрицательными")
    private BigDecimal fatPer100g;

    @NotNull(message = "Углеводы обязательны")
    @PositiveOrZero(message = "Углеводы не могут быть отрицательными")
    private BigDecimal carbsPer100g;

    @NotNull(message = "Категория обязательна")
    private Integer categoryId;
}