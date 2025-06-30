package ru.naumen.calorietracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.dto.ExerciseCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseResponse;
import ru.naumen.calorietracker.dto.ExerciseUpdateRequest;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.service.ExerciseService;
import ru.naumen.calorietracker.util.AuthUtils;

import java.security.Principal;


/**
 * Контроллер для работы с упражнениями.
 * Позволяет просматривать, создавать, обновлять и удалять упражнения.
 * Поиск упражнений по имени также поддерживается.
 */
@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final AuthUtils authUtils;

    /**
     * Получить постраничный список всех упражнений.
     *
     * @param page номер страницы (начинается с 0)
     * @param size количество элементов на странице
     * @return страница с упражнениями
     */
    @GetMapping
    public Page<ExerciseResponse> getAllExercises(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return exerciseService.getAllExercises(pageable);
    }

    /**
     * Получить упражнение по его идентификатору.
     *
     * @param id ID упражнения
     * @return информация об упражнении
     */
    @GetMapping("/{id}")
    public ExerciseResponse getExerciseById(@PathVariable Integer id) {
        return exerciseService.getExerciseById(id);
    }

    /**
     * Создать новое упражнение.
     *
     * @param request данные для создания упражнения
     * @param principal текущий аутентифицированный пользователь
     * @return созданное упражнение
     */
    @PostMapping
    public ExerciseResponse createExercise(@Valid @RequestBody ExerciseCreateRequest request, Principal principal) {
        User currentUser = authUtils.getAuthenticatedUser(principal);
        return exerciseService.createExercise(request, currentUser);
    }

    /**
     * Обновить существующее упражнение.
     *
     * @param request данные для обновления упражнения
     * @param principal текущий аутентифицированный пользователь
     * @return обновлённое упражнение
     */
    @PutMapping
    public ExerciseResponse updateExercise(@Valid @RequestBody ExerciseUpdateRequest request, Principal principal) {
        User currentUser = authUtils.getAuthenticatedUser(principal);
        return exerciseService.updateExercise(request, currentUser);
    }

    /**
     * Удалить упражнение по ID.
     * Доступно только пользователям с ролью ADMIN.
     *
     * @param id ID упражнения для удаления
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteExercise(@PathVariable Integer id) {
        exerciseService.deleteExercise(id);
    }

    /**
     * Поиск упражнений по названию с пагинацией.
     *
     * @param name часть или полное имя для поиска
     * @param page номер страницы (начинается с 0)
     * @param size количество элементов на странице
     * @return страница с найденными упражнениями
     */
    @GetMapping("/search")
    public Page<ExerciseResponse> search(@RequestParam String name,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return exerciseService.searchByName(name, pageable);
    }
} 