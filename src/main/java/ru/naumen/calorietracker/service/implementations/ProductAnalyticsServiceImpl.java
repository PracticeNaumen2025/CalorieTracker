package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.naumen.calorietracker.dto.CategoryPopularityResponse;
import ru.naumen.calorietracker.dto.ProductAveragePortionResponse;
import ru.naumen.calorietracker.dto.ProductNutritionSummaryResponse;
import ru.naumen.calorietracker.dto.ProductPopularityResponse;
import ru.naumen.calorietracker.repository.FoodEntryRepository;
import ru.naumen.calorietracker.service.ProductAnalyticsService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAnalyticsServiceImpl implements ProductAnalyticsService {
    private final FoodEntryRepository foodEntryRepository;

    public List<ProductPopularityResponse> getPopularProducts(LocalDate from, LocalDate to, int limit) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);
        List<Object[]> rows = foodEntryRepository.findPopularProducts(start, end);
        return rows.stream()
                .map(r -> new ProductPopularityResponse((Integer) r[0], (String) r[1], (Long) r[2]))
                .limit(limit)
                .toList();
    }

    public List<ProductAveragePortionResponse> getAveragePortionSizes(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);
        List<Object[]> rows = foodEntryRepository.findAveragePortionSize(start, end);
        return rows.stream()
                .map(r -> new ProductAveragePortionResponse((Integer) r[0], (String) r[1], ((Double) r[2])))
                .toList();
    }

    public List<ProductNutritionSummaryResponse> getNutritionSummaryByProduct(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);
        List<Object[]> rows = foodEntryRepository.findNutritionSummaryByProduct(start, end);
        return rows.stream()
                .map(r -> new ProductNutritionSummaryResponse(
                        (Integer) r[0],
                        (String) r[1],
                        (BigDecimal) r[2],
                        (BigDecimal) r[3],
                        (BigDecimal) r[4],
                        (BigDecimal) r[5]
                ))
                .toList();
    }

    public List<CategoryPopularityResponse> getPopularCategories(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);
        List<Object[]> rows = foodEntryRepository.findPopularCategories(start, end);
        return rows.stream()
                .map(r -> new CategoryPopularityResponse((String) r[0], (Long) r[1]))
                .toList();
    }

    @Override
    public BigDecimal getAverageDailyCaloriesConsumed(LocalDate from, LocalDate to) {

        if (to.isBefore(from)) {
            return BigDecimal.ZERO;
        }

        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);
        BigDecimal totalCalories = foodEntryRepository.sumCaloriesBetweenDates(start, end);
        long days = ChronoUnit.DAYS.between(from, to) + 1;
        if (days <= 0) return BigDecimal.ZERO;
        return totalCalories.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);
    }
}
