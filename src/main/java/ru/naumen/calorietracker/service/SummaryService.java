package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.DaySummaryRequest;
import ru.naumen.calorietracker.dto.DaySummaryResponse;
import ru.naumen.calorietracker.dto.PeriodSummaryRequest;
import java.util.List;

/**
 * Сервис для получения сводной информации по дням и периодам.
 */
public interface SummaryService {
    /**
     * Возвращает сводку за конкретный день для указанного пользователя.
     * @param userId Идентификатор пользователя.
     * @param request Запрос, содержащий дату.
     * @return Объект DaySummaryResponse, содержащий сводку за день.
     */
    DaySummaryResponse getDaySummary(Integer userId, DaySummaryRequest request);
    /**
     * Возвращает сводку за указанный период для конкретного пользователя.
     * @param userId Идентификатор пользователя.
     * @param request Запрос, содержащий начальную и конечную даты периода.
     * @return Список объектов DaySummaryResponse, содержащих сводки за каждый день периода.
     */
    List<DaySummaryResponse> getPeriodSummary(Integer userId, PeriodSummaryRequest request);
}
