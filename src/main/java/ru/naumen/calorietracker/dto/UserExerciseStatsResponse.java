package ru.naumen.calorietracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class UserExerciseStatsResponse {
    private Integer userId;
    private String userName;
    private Long workoutCount;
    private BigDecimal caloriesBurned;
}
