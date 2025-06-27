package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для аналитики данных по пользователям.
 */
public interface UserAnalyticsService {
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
}
