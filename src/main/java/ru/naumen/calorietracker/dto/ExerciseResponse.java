package ru.naumen.calorietracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseResponse {
    private Integer exerciseId;
    private String name;
    private Integer caloriesPerHour;
} 