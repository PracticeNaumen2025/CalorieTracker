package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Сервис для получения административной статистики.
 */
public interface AdminStatsService {
    /**
     * Возвращает общую статистику по пользователям.
     * @return Объект UserStatisticsResponse, содержащий статистику пользователей.
     */
    UserStatisticsResponse getUserStatistics();

    /**
     * Возвращает демографические данные пользователей.
     * @return Объект UserDemographicsResponse, содержащий демографические данные.
     */
    UserDemographicsResponse getUserDemographics();

    /**
     * Возвращает статистику регистраций пользователей за указанный период.
     * @param from Начальная дата периода.
     * @param to Конечная дата периода.
     * @return Список UserRegistrationStatsResponse, содержащий статистику регистраций.
     */
    List<UserRegistrationStatsResponse> getRegistrationStats(LocalDate from, LocalDate to);

    /**
     * Возвращает количество активных пользователей за последние N дней.
     * @param days Количество дней для определения активности.
     * @return Объект ActiveUsersResponse, содержащий количество активных пользователей.
     */
    ActiveUsersResponse getActiveUsers(int days);

    /**
     * Возвращает список популярных продуктов за указанный период.
     * @param from Начальная дата периода.
     * @param to Конечная дата периода.
     * @param limit Максимальное количество продуктов для возврата.
     * @return Список ProductPopularityResponse, содержащий популярные продукты.
     */
    List<ProductPopularityResponse> getPopularProducts(LocalDate from, LocalDate to, int limit);

    /**
     * Возвращает средние размеры порций продуктов за указанный период.
     * @param from Начальная дата периода.
     * @param to Конечная дата периода.
     * @return Список ProductAveragePortionResponse, содержащий средние размеры порций.
     */
    List<ProductAveragePortionResponse> getAveragePortionSizes(LocalDate from, LocalDate to);
    /**
     * Возвращает сводку по питательным веществам для продуктов за указанный период.
     * @param from Начальная дата периода.
     * @param to Конечная дата периода.
     * @return Список ProductNutritionSummaryResponse, содержащий сводку по питательным веществам.
     */
    List<ProductNutritionSummaryResponse> getNutritionSummaryByProduct(LocalDate from, LocalDate to);
    /**
     * Возвращает список популярных категорий продуктов за указанный период.
     * @param from Начальная дата периода.
     * @param to Конечная дата периода.
     * @return Список CategoryPopularityResponse, содержащий популярные категории.
     */
    List<CategoryPopularityResponse> getPopularCategories(LocalDate from, LocalDate to);
    /**
     * Возвращает среднее количество потребляемых калорий в день за указанный период.
     * @param from Начальная дата периода.
     * @param to Конечная дата периода.
     * @return Среднее количество калорий в день.
     */
    BigDecimal getAverageDailyCaloriesConsumed(LocalDate from, LocalDate to);

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
