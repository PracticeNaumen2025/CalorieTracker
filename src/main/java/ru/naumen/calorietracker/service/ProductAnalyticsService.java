package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ProductAnalyticsService {
    List<ProductPopularityResponse> getPopularProducts(LocalDate from, LocalDate to, int limit);
    List<ProductAveragePortionResponse> getAveragePortionSizes(LocalDate from, LocalDate to);
    List<ProductNutritionSummaryResponse> getNutritionSummaryByProduct(LocalDate from, LocalDate to);
    List<CategoryPopularityResponse> getPopularCategories(LocalDate from, LocalDate to);
    BigDecimal getAverageDailyCaloriesConsumed(LocalDate from, LocalDate to);
}
