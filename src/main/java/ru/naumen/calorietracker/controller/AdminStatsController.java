package ru.naumen.calorietracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.dto.*;
import ru.naumen.calorietracker.service.AdminStatsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    @Operation(description = "Возвращает общее количество пользователей, активных и удалённых, а также распределение по ролям.")
    @GetMapping("/users/statistics")
    public UserStatisticsResponse getUserStatistics() {
        return adminStatsService.getUserStatistics();
    }

    @Operation(description = "Возвращает информацию о распределении по полу, возрасту, средний рост и вес пользователей.")
    @GetMapping("/users/demographics")
    public UserDemographicsResponse getUserDemographics() {
        return adminStatsService.getUserDemographics();
    }

    @Operation(description = "Возвращает список с количеством регистраций пользователей по дням за указанный период.")
    @GetMapping("/users/registrations")
    public List<UserRegistrationStatsResponse> getRegistrationStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getRegistrationStats(from, to);
    }

    @Operation(description = "Возвращает количество уникальных пользователей, которые вносили записи в приложении за последние N дней.")
    @GetMapping("/users/active")
    public ActiveUsersResponse getActiveUsers(@RequestParam int days) {
        return adminStatsService.getActiveUsers(days);
    }

    @Operation(description = "Возвращает список популярных продуктов по количеству использований за период.")
    @GetMapping("/products/popular")
    public List<ProductPopularityResponse> getPopularProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return adminStatsService.getPopularProducts(from, to, limit);
    }

    @Operation(description = "Возвращает средний размер порции продуктов за период.")
    @GetMapping("/products/average-portion")
    public List<ProductAveragePortionResponse> getAveragePortionSizes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getAveragePortionSizes(from, to);
    }

    @Operation(description = "Возвращает суммарное потребление калорий и БЖУ по продуктам за период.")
    @GetMapping("/products/nutrition-summary")
    public List<ProductNutritionSummaryResponse> getNutritionSummaryByProduct(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getNutritionSummaryByProduct(from, to);
    }

    @Operation(description = "Возвращает популярность категорий продуктов за период.")
    @GetMapping("/products/popular-categories")
    public List<CategoryPopularityResponse> getPopularCategories(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getPopularCategories(from, to);
    }

    @Operation(description = "Возвращает среднее количество употребленных калорий в день за указанный период.")
    @GetMapping("/products/average-daily-calories")
    public BigDecimal getAverageDailyCalories(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getAverageDailyCaloriesConsumed(from, to);
    }

    @Operation(description = "Возвращает общее количество записей о тренировках за период.")
    @GetMapping("/exercises/total-entries")
    public long getTotalExerciseEntries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getTotalExerciseEntries(from, to);
    }

    @Operation(description = "Возвращает топ популярных упражнений по количеству выполнений за период.")
    @GetMapping("/exercises/popular")
    public List<ExercisePopularityResponse> getPopularExercises(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return adminStatsService.getPopularExercises(from, to, limit);
    }

    @Operation(description = "Возвращает суммарное количество сожжённых калорий за период.")
    @GetMapping("/exercises/total-calories")
    public BigDecimal getTotalCaloriesBurned(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getTotalCaloriesBurned(from, to);
    }

    @Operation(description = "Возвращает среднее количество сожжённых калорий за тренировку за период.")
    @GetMapping("/exercises/average-calories")
    public BigDecimal getAverageCaloriesPerExercise(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getAverageCaloriesPerExercise(from, to);
    }

    @Operation(description = "Возвращает топ пользователей по суммарным сожжённым калориям за период.")
    @GetMapping("/exercises/top-users")
    public List<UserExerciseStatsResponse> getTopUsersByCaloriesBurned(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return adminStatsService.getTopUsersByCaloriesBurned(from, to, limit);
    }

    @Operation(description = "Возвращает распределение тренировок по времени суток за период (утро, день, вечер).")
    @GetMapping("/exercises/time-of-day-distribution")
    public Map<String, Long> getExerciseCountByTimeOfDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getExerciseCountByTimeOfDay(from, to);
    }
}
