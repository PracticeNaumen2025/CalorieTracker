package ru.naumen.calorietracker.controller;

import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import ru.naumen.calorietracker.dto.ExerciseEntryCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseEntryResponse;
import ru.naumen.calorietracker.dto.ExerciseEntryUpdateRequest;
import ru.naumen.calorietracker.service.ExerciseEntryService;

import java.security.Principal;

/**
 * REST-контроллер для управления записями тренировок (Exercise Entries).
 * Позволяет создавать, получать и обновлять записи упражнений пользователя.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/exercise/entry")
public class ExerciseEntryController {

    private final ExerciseEntryService exerciseEntryService;
    private final AuthUtils authUtils;

    /**
     * Получить запись тренировки по её ID.
     *
     * @param id ID записи тренировки
     * @param principal текущий аутентифицированный пользователь
     * @return данные записи тренировки
     */
    @GetMapping("/{id}")
    public ExerciseEntryResponse getExerciseEntry(@PathVariable Integer id, Principal principal) {
        User currentUser = authUtils.getAuthenticatedUser(principal);
        return exerciseEntryService.getExerciseEntryById(id, currentUser);
    }

    /**
     * Получить список записей тренировок пользователя за указанный период.
     *
     * @param start начальная дата и время в формате ISO (например, 2025-06-30T00:00:00)
     * @param end конечная дата и время в формате ISO
     * @param principal текущий аутентифицированный пользователь
     * @return список записей тренировок
     */
    @GetMapping
    public java.util.List<ExerciseEntryResponse> getEntriesByDate(@RequestParam("start") String start,
                                                                 @RequestParam("end") String end,
                                                                 Principal principal) {
        User currentUser = authUtils.getAuthenticatedUser(principal);
        java.time.LocalDateTime startDate = java.time.LocalDateTime.parse(start);
        java.time.LocalDateTime endDate = java.time.LocalDateTime.parse(end);
        return exerciseEntryService.getExerciseEntriesByDate(startDate, endDate, currentUser);
    }

    /**
     * Создать новую запись тренировки.
     *
     * @param request данные для создания записи тренировки
     * @param principal текущий аутентифицированный пользователь
     * @return созданная запись тренировки
     */
    @PostMapping
    public ExerciseEntryResponse createEntry(@RequestBody ExerciseEntryCreateRequest request, Principal principal) {
        User currentUser = authUtils.getAuthenticatedUser(principal);
        return exerciseEntryService.createExerciseEntry(request, currentUser);
    }

    /**
     * Обновить существующую запись тренировки.
     *
     * @param request данные для обновления записи тренировки
     * @param principal текущий аутентифицированный пользователь
     * @return обновлённая запись тренировки
     */
    @PutMapping
    public ExerciseEntryResponse updateEntry(@RequestBody ExerciseEntryUpdateRequest request, Principal principal) {
        User currentUser = authUtils.getAuthenticatedUser(principal);
        return exerciseEntryService.updateExerciseEntry(request, currentUser);
    }
}
