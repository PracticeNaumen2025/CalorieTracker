package ru.naumen.calorietracker.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExerciseEntryUpdateRequest {
    private Integer entryId;
    private Integer exerciseId;
    private LocalDateTime dateTime;
    private Integer durationMinutes;
} 