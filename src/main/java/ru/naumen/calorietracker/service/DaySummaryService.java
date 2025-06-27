package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.DaySummaryDTO;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDate;

/**
 * Сервис для работы с ежедневными сводками по питанию.
 */
public interface DaySummaryService {
    /**
     * Возвращает сводку по питанию для конкретного пользователя и даты.
     * @param user Пользователь, для которого запрашивается сводка.
     * @param date Дата, за которую запрашивается сводка.
     * @return Объект DaySummaryDTO, содержащий сводку за указанный день.
     */
    DaySummaryDTO getSummaryByUserAndDate(User user, LocalDate date);
}
