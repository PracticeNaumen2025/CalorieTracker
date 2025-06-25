package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AdminStatsService {
    UserStatisticsResponse getUserStatistics();
    UserDemographicsResponse getUserDemographics();
    List<UserRegistrationStatsResponse> getRegistrationStats(LocalDate from, LocalDate to);
    ActiveUsersResponse getActiveUsers(int days);

    List<ProductPopularityResponse> getPopularProducts(LocalDate from, LocalDate to, int limit);
    List<ProductAveragePortionResponse> getAveragePortionSizes(LocalDate from, LocalDate to);
    List<ProductNutritionSummaryResponse> getNutritionSummaryByProduct(LocalDate from, LocalDate to);
    List<CategoryPopularityResponse> getPopularCategories(LocalDate from, LocalDate to);
    BigDecimal getAverageDailyCaloriesConsumed(LocalDate from, LocalDate to);

    long getTotalExerciseEntries(LocalDate from, LocalDate to);
    List<ExercisePopularityResponse> getPopularExercises(LocalDate from, LocalDate to, int limit);
    BigDecimal getTotalCaloriesBurned(LocalDate from, LocalDate to);
    BigDecimal getAverageCaloriesPerExercise(LocalDate from, LocalDate to);
    List<UserExerciseStatsResponse> getTopUsersByCaloriesBurned(LocalDate from, LocalDate to, int limit);
    Map<String, Long> getExerciseCountByTimeOfDay(LocalDate from, LocalDate to);
}
