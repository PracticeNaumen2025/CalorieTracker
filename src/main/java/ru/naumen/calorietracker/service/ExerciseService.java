package ru.naumen.calorietracker.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.naumen.calorietracker.dto.ExerciseCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseResponse;
import ru.naumen.calorietracker.dto.ExerciseUpdateRequest;
import ru.naumen.calorietracker.model.User;

/**
 * Сервис для работы с упражнениями.
 */
public interface ExerciseService {
    /**
     * Возвращает все упражнения с пагинацией.
     * @param pageable Объект Pageable для пагинации.
     * @return Страница объектов ExerciseResponse.
     */
    Page<ExerciseResponse> getAllExercises(Pageable pageable);
    /**
     * Возвращает упражнение по его идентификатору.
     * @param id Идентификатор упражнения.
     * @return Объект ExerciseResponse, представляющий упражнение.
     */
    ExerciseResponse getExerciseById(Integer id);
    /**
     * Создает новое упражнение.
     * @param request Запрос на создание упражнения.
     * @param currentUser Текущий пользователь, создающий упражнение.
     * @return Объект ExerciseResponse, представляющий созданное упражнение.
     */
    ExerciseResponse createExercise(ExerciseCreateRequest request, User currentUser);
    /**
     * Обновляет существующее упражнение.
     * @param request Запрос на обновление упражнения.
     * @param currentUser Текущий пользователь, обновляющий упражнение.
     * @return Объект ExerciseResponse, представляющий обновленное упражнение.
     */
    ExerciseResponse updateExercise(ExerciseUpdateRequest request, User currentUser);
    /**
     * Удаляет упражнение по его идентификатору.
     * @param id Идентификатор упражнения для удаления.
     */
    void deleteExercise(Integer id);
    /**
     * Ищет упражнения по имени с пагинацией.
     * @param name Часть имени упражнения для поиска.
     * @param pageable Объект Pageable для пагинации.
     * @return Страница объектов ExerciseResponse, соответствующих критериям поиска.
     */
    Page<ExerciseResponse> searchByName(String name, Pageable pageable);
} 