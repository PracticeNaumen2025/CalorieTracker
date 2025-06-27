package ru.naumen.calorietracker.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.naumen.calorietracker.dto.PageResponse;
import ru.naumen.calorietracker.dto.UserGoalCreateRequest;
import ru.naumen.calorietracker.dto.UserGoalResponse;
import ru.naumen.calorietracker.dto.UserGoalUpdateRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для управления целями пользователя.
 */
public interface UserGoalService {
    /**
     * Создает новую цель для пользователя.
     * @param request Запрос на создание цели пользователя.
     * @param userId Идентификатор пользователя, для которого создается цель.
     * @return Объект UserGoalResponse, представляющий созданную цель.
     */
    UserGoalResponse createUserGoal(UserGoalCreateRequest request, Integer userId);
    /**
     * Обновляет существующую цель пользователя.
     * @param request Запрос на обновление цели пользователя.
     * @param userId Идентификатор пользователя, обновляющего цель.
     * @return Объект UserGoalResponse, представляющий обновленную цель.
     */
    UserGoalResponse updateUserGoal(UserGoalUpdateRequest request, Integer userId);
    /**
     * Возвращает цель пользователя по его идентификатору и дате.
     * @param ownerUserId Идентификатор владельца цели.
     * @param currentUserId Идентификатор текущего пользователя (для проверки доступа).
     * @param date Дата, для которой запрашивается цель.
     * @return Объект UserGoalResponse, представляющий цель на указанную дату.
     */
    UserGoalResponse getUserGoalByUserIdAndDate(Integer ownerUserId, Integer currentUserId, LocalDate date);
    /**
     * Возвращает цели пользователя по его идентификатору с пагинацией.
     * @param ownerUserId Идентификатор владельца целей.
     * @param currentUserId Идентификатор текущего пользователя (для проверки доступа).
     * @param pageable Объект Pageable для пагинации.
     * @return Страница объектов UserGoalResponse.
     */
    PageResponse<UserGoalResponse> getUserGoalsByUserId(Integer ownerUserId, Integer currentUserId, Pageable pageable);
    /**
     * Удаляет цель пользователя по ее идентификатору.
     * @param userId Идентификатор пользователя, которому принадлежит цель.
     * @param goalId Идентификатор цели для удаления.
     */
    void deleteUserGoalByUserId(Integer userId, Integer goalId);
}
