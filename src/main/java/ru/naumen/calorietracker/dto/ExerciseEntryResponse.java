package ru.naumen.calorietracker.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExerciseEntryResponse {
    private Integer entryId;
    private Integer userId;
    private Integer exerciseId;
    private LocalDateTime dateTime;
    private Integer durationMinutes;
    private BigDecimal caloriesBurned;
} 