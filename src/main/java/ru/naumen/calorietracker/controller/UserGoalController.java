package ru.naumen.calorietracker.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.dto.PageResponse;
import ru.naumen.calorietracker.dto.UserGoalCreateRequest;
import ru.naumen.calorietracker.dto.UserGoalResponse;
import ru.naumen.calorietracker.dto.UserGoalUpdateRequest;
import ru.naumen.calorietracker.service.UserGoalService;

import java.security.Principal;
import java.time.LocalDate;

/**
 * REST-контроллер для управления целями пользователя (User Goals).
 * Позволяет создавать, обновлять, получать и удалять цели.
 * Все операции выполняются от имени аутентифицированного пользователя.
 */
@RestController
@RequestMapping("/api/goals")
public class UserGoalController extends BaseController {

    private final UserGoalService userGoalService;

    @Autowired
    public UserGoalController(UserGoalService userGoalService) {
        this.userGoalService = userGoalService;
    }

    /**
     * Создать новую цель пользователя.
     *
     * @param request   данные для создания цели
     * @param principal текущий аутентифицированный пользователь
     * @return созданная цель
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserGoalResponse createGoal(
            @RequestBody @Valid UserGoalCreateRequest request,
            Principal principal) {
        Integer currentUserId = getUserIdFromPrincipal(principal);
        return userGoalService.createUserGoal(request, currentUserId);
    }

    /**
     * Обновить существующую цель пользователя.
     *
     * @param request   данные для обновления цели
     * @param principal текущий аутентифицированный пользователь
     * @return обновленная цель
     */
    @PutMapping
    public UserGoalResponse updateGoal(
            @RequestBody @Valid UserGoalUpdateRequest request,
            Principal principal) {
        Integer currentUserId = getUserIdFromPrincipal(principal);
        return userGoalService.updateUserGoal(request, currentUserId);
    }

    /**
     * Получить список целей пользователя с пагинацией.
     *
     * @param ownerUserId ID пользователя, чьи цели запрашиваются
     * @param page        номер страницы, по умолчанию 0
     * @param size        размер страницы, по умолчанию 10
     * @param principal   текущий аутентифицированный пользователь
     * @return страница с целями пользователя
     */
    @GetMapping("/{ownerUserId}")
    public PageResponse<UserGoalResponse> getGoals(
            @PathVariable Integer ownerUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal
    ) {
        Integer currentUserId = getUserIdFromPrincipal(principal);
        Pageable pageable = PageRequest.of(page, size);
        return userGoalService.getUserGoalsByUserId(ownerUserId, currentUserId, pageable);
    }

    /**
     * Получить активную цель пользователя на указанную дату.
     *
     * @param date      дата для поиска цели
     * @param principal текущий аутентифицированный пользователь
     * @return цель, активная на заданную дату
     */
    @GetMapping("/active")
    public UserGoalResponse getGoalByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Principal principal
    ) {
        Integer currentUserId = getUserIdFromPrincipal(principal);
        return userGoalService.getUserGoalByUserIdAndDate(currentUserId, currentUserId, date);
    }

    /**
     * Удалить цель пользователя по ID.
     *
     * @param goalId    ID цели для удаления
     * @param principal текущий аутентифицированный пользователь
     */
    @DeleteMapping("/{goalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGoals(
            @PathVariable Integer goalId,
            Principal principal) {
        Integer userid = getUserIdFromPrincipal(principal);
        userGoalService.deleteUserGoalByUserId(userid, goalId);
    }

}
