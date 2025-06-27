package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.ExerciseEntryCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseEntryResponse;
import ru.naumen.calorietracker.dto.ExerciseEntryUpdateRequest;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с записями о физической активности.
 */
public interface ExerciseEntryService {
    /**
     * Возвращает запись о физической активности по ее идентификатору для указанного пользователя.
     * @param id Идентификатор записи о физической активности.
     * @param user Пользователь, которому принадлежит запись.
     * @return Объект ExerciseEntryResponse, представляющий запись.
     */
    ExerciseEntryResponse getExerciseEntryById(Integer id, User user);

    /**
     * Возвращает все записи о физической активности текущего пользователя за указанный период.
     * @param start Начало периода.
     * @param end Конец периода.
     * @param user Пользователь, для которого запрашиваются записи.
     * @return Список объектов ExerciseEntryResponse.
     */
    List<ExerciseEntryResponse> getExerciseEntriesByDate(LocalDateTime start, LocalDateTime end, User user);

    /**
     * Создает новую запись о физической активности.
     * @param request Запрос на создание записи о физической активности.
     * @param user Пользователь, создающий запись.
     * @return Объект ExerciseEntryResponse, представляющий созданную запись.
     */
    ExerciseEntryResponse createExerciseEntry(ExerciseEntryCreateRequest request, User user);

    /**
     * Редактирует существующую запись о физической активности.
     * @param request Запрос на обновление записи о физической активности.
     * @param user Пользователь, обновляющий запись.
     * @return Объект ExerciseEntryResponse, представляющий обновленную запись.
     */
    ExerciseEntryResponse updateExerciseEntry(ExerciseEntryUpdateRequest request, User user);
}
