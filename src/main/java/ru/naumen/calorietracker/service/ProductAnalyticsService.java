package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для аналитики данных по продуктам.
 */
public interface ProductAnalyticsService {
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
}
