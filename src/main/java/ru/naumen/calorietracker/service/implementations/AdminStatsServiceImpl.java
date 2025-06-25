package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.naumen.calorietracker.dto.*;
import ru.naumen.calorietracker.service.AdminStatsService;
import ru.naumen.calorietracker.service.ExerciseAnalyticsService;
import ru.naumen.calorietracker.service.ProductAnalyticsService;
import ru.naumen.calorietracker.service.UserAnalyticsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminStatsServiceImpl implements AdminStatsService {
    private final UserAnalyticsService userAnalyticsService;
    private final ProductAnalyticsService productAnalyticsService;
    private final ExerciseAnalyticsService exerciseAnalyticsService;

    @Override
    public UserStatisticsResponse getUserStatistics() {
        return userAnalyticsService.getUserStatistics();
    }

    @Override
    public UserDemographicsResponse getUserDemographics() {
        return userAnalyticsService.getUserDemographics();
    }

    @Override
    public List<UserRegistrationStatsResponse> getRegistrationStats(LocalDate from, LocalDate to) {
        return userAnalyticsService.getRegistrationStats(from, to);
    }

    @Override
    public ActiveUsersResponse getActiveUsers(int days) {
        return userAnalyticsService.getActiveUsers(days);
    }

    @Override
    public List<ProductPopularityResponse> getPopularProducts(LocalDate from, LocalDate to, int limit) {
        return productAnalyticsService.getPopularProducts(from, to, limit);
    }

    @Override
    public List<ProductAveragePortionResponse> getAveragePortionSizes(LocalDate from, LocalDate to) {
        return productAnalyticsService.getAveragePortionSizes(from, to);
    }

    @Override
    public List<ProductNutritionSummaryResponse> getNutritionSummaryByProduct(LocalDate from, LocalDate to) {
        return productAnalyticsService.getNutritionSummaryByProduct(from, to);
    }

    @Override
    public List<CategoryPopularityResponse> getPopularCategories(LocalDate from, LocalDate to) {
        return productAnalyticsService.getPopularCategories(from, to);
    }

    @Override
    public BigDecimal getAverageDailyCaloriesConsumed(LocalDate from, LocalDate to) {
        return productAnalyticsService.getAverageDailyCaloriesConsumed(from, to);
    }

    @Override
    public long getTotalExerciseEntries(LocalDate from, LocalDate to) {
        return exerciseAnalyticsService.getTotalExerciseEntries(from, to);
    }

    @Override
    public List<ExercisePopularityResponse> getPopularExercises(LocalDate from, LocalDate to, int limit) {
        return exerciseAnalyticsService.getPopularExercises(from, to, limit);
    }

    @Override
    public BigDecimal getTotalCaloriesBurned(LocalDate from, LocalDate to) {
        return exerciseAnalyticsService.getTotalCaloriesBurned(from, to);
    }

    @Override
    public BigDecimal getAverageCaloriesPerExercise(LocalDate from, LocalDate to) {
        return exerciseAnalyticsService.getAverageCaloriesPerExercise(from, to);
    }

    @Override
    public List<UserExerciseStatsResponse> getTopUsersByCaloriesBurned(LocalDate from, LocalDate to, int limit) {
        return exerciseAnalyticsService.getTopUsersByCaloriesBurned(from, to, limit);
    }

    @Override
    public Map<String, Long> getExerciseCountByTimeOfDay(LocalDate from, LocalDate to) {
        return exerciseAnalyticsService.getExerciseCountByTimeOfDay(from, to);
    }
}


