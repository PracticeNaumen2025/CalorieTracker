package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.ExerciseEntryCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseEntryResponse;
import ru.naumen.calorietracker.dto.ExerciseEntryUpdateRequest;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ExerciseEntryService {
    /**
     * Получить запись о физической активности по id
     */
    ExerciseEntryResponse getExerciseEntryById(Integer id, User user);

    /**
     * Получить все записи о физической активности текущего пользователя, произошедших за данный период
     */
    List<ExerciseEntryResponse> getExerciseEntriesByDate(LocalDateTime start, LocalDateTime end, User user);

    /**
     * Создать запись о физической активности
     */
    ExerciseEntryResponse createExerciseEntry(ExerciseEntryCreateRequest request, User user);

    /**
     * Редактировать запись о физической активности
     */
    ExerciseEntryResponse updateExerciseEntry(ExerciseEntryUpdateRequest request, User user);
}
