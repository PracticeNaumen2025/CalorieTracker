package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExerciseAnalyticsService {
    long getTotalExerciseEntries(LocalDate from, LocalDate to);
    List<ExercisePopularityResponse> getPopularExercises(LocalDate from, LocalDate to, int limit);
    BigDecimal getTotalCaloriesBurned(LocalDate from, LocalDate to);
    BigDecimal getAverageCaloriesPerExercise(LocalDate from, LocalDate to);
    List<UserExerciseStatsResponse> getTopUsersByCaloriesBurned(LocalDate from, LocalDate to, int limit);
    Map<String, Long> getExerciseCountByTimeOfDay(LocalDate from, LocalDate to);
}
