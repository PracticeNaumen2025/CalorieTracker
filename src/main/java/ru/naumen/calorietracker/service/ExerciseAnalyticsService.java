package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Сервис для аналитики данных по упражнениям.
 */
public interface ExerciseAnalyticsService {
    /**
     * Возвращает общее количество записей об упражнениях за указанный период.
     * @param from Начальная дата периода.
     * @param to Конечная дата периода.
     * @return Общее количество записей об упражнениях.
     */
    long getTotalExerciseEntries(LocalDate from, LocalDate to);
    /**
     * Возвращает список популярных упражнений за указанный период.
     * @param from Начальная дата периода.
     * @param to Конечная дата периода.
     * @param limit Максимальное количество упражнений для возврата.
     * @return Список ExercisePopularityResponse, содержащий популярные упражнения.
     */
    List<ExercisePopularityResponse> getPopularExercises(LocalDate from, LocalDate to, int limit);
    /**
     * Возвращает общее количество сожженных калорий за указанный период.
     * @param from Начальная дата периода.
     * @param to Конечная дата периода.
     * @return Общее количество сожженных калорий.
     */
    BigDecimal getTotalCaloriesBurned(LocalDate from, LocalDate to);
    /**
     * Возвращает среднее количество калорий, сжигаемых за одно упражнение, за указанный период.
     * @param from Начальная дата периода.
     * @param to Конечная дата периода.
     * @return Среднее количество калорий за упражнение.
     */
    BigDecimal getAverageCaloriesPerExercise(LocalDate from, LocalDate to);
    /**
     * Возвращает список пользователей с наибольшим количеством сожженных калорий за указанный период.
     * @param from Начальная дата периода.
     * @param to Конечная дата периода.
     * @param limit Максимальное количество пользователей для возврата.
     * @return Список UserExerciseStatsResponse, содержащий топ пользователей по сожженным калориям.
     */
    List<UserExerciseStatsResponse> getTopUsersByCaloriesBurned(LocalDate from, LocalDate to, int limit);
    /**
     * Возвращает количество упражнений по времени суток за указанный период.
     * @param from Начальная дата периода.
     * @param to Конечная дата периода.
     * @return Карта, где ключ - время суток, значение - количество упражнений.
     */
    Map<String, Long> getExerciseCountByTimeOfDay(LocalDate from, LocalDate to);
}
