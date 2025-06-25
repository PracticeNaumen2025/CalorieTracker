package ru.naumen.calorietracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExercisePopularityResponse {
    private Integer exerciseId;
    private String exerciseName;
    private Long usageCount;
}
