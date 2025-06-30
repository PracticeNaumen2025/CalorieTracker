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

/**
 * REST контроллер для административной статистики и аналитики.
 * Доступен только пользователям с ролью ADMIN.
 * Предоставляет метрики по пользователям, продуктам, упражнениям за указанные периоды.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    /**
     * Возвращает статистику пользователей: общее число, активные, удалённые, распределение по ролям.
     */
    @Operation(description = "Возвращает общее количество пользователей, активных и удалённых, а также распределение по ролям.")
    @GetMapping("/users/statistics")
    public UserStatisticsResponse getUserStatistics() {
        return adminStatsService.getUserStatistics();
    }

    /**
     * Возвращает демографические данные пользователей: пол, возраст, средний рост и вес.
     */
    @Operation(description = "Возвращает информацию о распределении по полу, возрасту, средний рост и вес пользователей.")
    @GetMapping("/users/demographics")
    public UserDemographicsResponse getUserDemographics() {
        return adminStatsService.getUserDemographics();
    }

    /**
     * Статистика регистраций пользователей по дням за указанный период.
     *
     * @param from начальная дата (включительно)
     * @param to конечная дата (включительно)
     * @return список статистики по дням
     */
    @Operation(description = "Возвращает список с количеством регистраций пользователей по дням за указанный период.")
    @GetMapping("/users/registrations")
    public List<UserRegistrationStatsResponse> getRegistrationStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getRegistrationStats(from, to);
    }

    /**
     * Количество уникальных активных пользователей за последние N дней.
     *
     * @param days количество дней для анализа активности
     * @return количество активных пользователей
     */
    @Operation(description = "Возвращает количество уникальных пользователей, которые вносили записи в приложении за последние N дней.")
    @GetMapping("/users/active")
    public ActiveUsersResponse getActiveUsers(@RequestParam int days) {
        return adminStatsService.getActiveUsers(days);
    }

    /**
     * Топ популярных продуктов по количеству использований за период.
     *
     * @param from дата начала периода
     * @param to дата конца периода
     * @param limit максимальное число возвращаемых продуктов
     * @return список популярных продуктов
     */
    @Operation(description = "Возвращает список популярных продуктов по количеству использований за период.")
    @GetMapping("/products/popular")
    public List<ProductPopularityResponse> getPopularProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return adminStatsService.getPopularProducts(from, to, limit);
    }

    /**
     * Средний размер порции продуктов за указанный период.
     */
    @Operation(description = "Возвращает средний размер порции продуктов за период.")
    @GetMapping("/products/average-portion")
    public List<ProductAveragePortionResponse> getAveragePortionSizes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getAveragePortionSizes(from, to);
    }

    /**
     * Суммарное потребление калорий и БЖУ по продуктам за период.
     */
    @Operation(description = "Возвращает суммарное потребление калорий и БЖУ по продуктам за период.")
    @GetMapping("/products/nutrition-summary")
    public List<ProductNutritionSummaryResponse> getNutritionSummaryByProduct(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getNutritionSummaryByProduct(from, to);
    }

    /**
     * Популярность категорий продуктов за период.
     */
    @Operation(description = "Возвращает популярность категорий продуктов за период.")
    @GetMapping("/products/popular-categories")
    public List<CategoryPopularityResponse> getPopularCategories(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getPopularCategories(from, to);
    }

    /**
     * Среднее количество употреблённых калорий в день за период.
     */
    @Operation(description = "Возвращает среднее количество употребленных калорий в день за указанный период.")
    @GetMapping("/products/average-daily-calories")
    public BigDecimal getAverageDailyCalories(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getAverageDailyCaloriesConsumed(from, to);
    }

    /**
     * Общее количество записей о тренировках за период.
     */
    @Operation(description = "Возвращает общее количество записей о тренировках за период.")
    @GetMapping("/exercises/total-entries")
    public long getTotalExerciseEntries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getTotalExerciseEntries(from, to);
    }

    /**
     * Топ популярных упражнений по количеству выполнений за период.
     */
    @Operation(description = "Возвращает топ популярных упражнений по количеству выполнений за период.")
    @GetMapping("/exercises/popular")
    public List<ExercisePopularityResponse> getPopularExercises(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return adminStatsService.getPopularExercises(from, to, limit);
    }

    /**
     * Суммарное количество сожжённых калорий за период.
     */
    @Operation(description = "Возвращает суммарное количество сожжённых калорий за период.")
    @GetMapping("/exercises/total-calories")
    public BigDecimal getTotalCaloriesBurned(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getTotalCaloriesBurned(from, to);
    }

    /**
     * Среднее количество сожжённых калорий за тренировку за период.
     */
    @Operation(description = "Возвращает среднее количество сожжённых калорий за тренировку за период.")
    @GetMapping("/exercises/average-calories")
    public BigDecimal getAverageCaloriesPerExercise(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getAverageCaloriesPerExercise(from, to);
    }

    /**
     * Топ пользователей по суммарным сожжённым калориям за период.
     */
    @Operation(description = "Возвращает топ пользователей по суммарным сожжённым калориям за период.")
    @GetMapping("/exercises/top-users")
    public List<UserExerciseStatsResponse> getTopUsersByCaloriesBurned(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return adminStatsService.getTopUsersByCaloriesBurned(from, to, limit);
    }

    /**
     * Распределение тренировок по времени суток (утро, день, вечер) за период.
     */
    @Operation(description = "Возвращает распределение тренировок по времени суток за период (утро, день, вечер).")
    @GetMapping("/exercises/time-of-day-distribution")
    public Map<String, Long> getExerciseCountByTimeOfDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return adminStatsService.getExerciseCountByTimeOfDay(from, to);
    }
}
