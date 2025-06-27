package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.UserMeasurementResponse;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для работы с измерениями пользователя (рост, вес).
 */
public interface UserMeasurementService {
    /**
     * Сохраняет ежедневные измерения пользователей (рост, вес) на указанную дату.
     * Если измерения на эту дату уже существуют, они обновляются, иначе создаются новые.
     * @param date Дата, для которой сохраняются измерения.
     */
    void saveDailyUserMeasurements(LocalDate date);
    /**
     * Возвращает все измерения пользователя, отсортированные по дате по возрастанию.
     * @param user Пользователь, для которого запрашиваются измерения.
     * @return Список объектов UserMeasurementResponse, содержащих измерения пользователя.
     */
    List<UserMeasurementResponse> getAllMeasurementsByUser(User user);
}
