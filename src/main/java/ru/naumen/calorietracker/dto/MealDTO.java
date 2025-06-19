package ru.naumen.calorietracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MealDTO {
    private Integer mealId;
    private LocalDateTime dateTime;
    private String mealType;
}
