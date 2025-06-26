package ru.naumen.calorietracker.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.naumen.calorietracker.dto.ExerciseCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseResponse;
import ru.naumen.calorietracker.dto.ExerciseUpdateRequest;
import ru.naumen.calorietracker.model.User;

public interface ExerciseService {
    Page<ExerciseResponse> getAllExercises(Pageable pageable);
    ExerciseResponse getExerciseById(Integer id);
    ExerciseResponse createExercise(ExerciseCreateRequest request, User currentUser);
    ExerciseResponse updateExercise(ExerciseUpdateRequest request, User currentUser);
    void deleteExercise(Integer id);
    Page<ExerciseResponse> searchByName(String name, Pageable pageable);
} 