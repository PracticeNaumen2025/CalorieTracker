package ru.naumen.calorietracker.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
public class ExerciseEntryCreateRequest {
    @NotNull(message = "Упражнение обязательно")
    private Integer exerciseId;
    @NotNull(message = "Дата и время обязательны")
    private LocalDateTime dateTime;
    @NotNull(message = "Длительность обязательна")
    @Positive(message = "Длительность должна быть положительной")
    private Integer durationMinutes;
} 