package ru.naumen.calorietracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.dto.*;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.service.DiaryService;
import ru.naumen.calorietracker.util.AuthUtils;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

/**
 * REST контроллер для управления дневником питания.
 * Позволяет создавать, изменять и удалять приёмы пищи и записи о еде,
 * а также получать сводки и списки по датам.
 */
@RestController
@RequestMapping("/api/diary")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DiaryController {
    private final AuthUtils authUtils;
    private final DiaryService diaryService;

    /**
     * Создаёт новый приём пищи для аутентифицированного пользователя.
     *
     * @param request тело запроса с данными о приёме пищи
     * @param principal информация об аутентифицированном пользователе
     * @return созданный приём пищи или ошибка
     */
    @PostMapping("/meal")
    public ResponseEntity<?> createMeal(@RequestBody CreateMealRequest request, Principal principal) {
        try {
            User user = authUtils.getAuthenticatedUser(principal);
            MealDTO mealDTO = diaryService.createMeal(request, user);
            return ResponseEntity.ok(mealDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Добавляет новую запись о еде в дневник.
     *
     * @param request данные о записи о еде
     * @return созданная запись или ошибка
     */
    @PostMapping("/food-entries")
    public ResponseEntity<?> addFoodEntry(@RequestBody AddFoodEntryRequest request) {
        try {
            FoodEntryDTO entryDTO = diaryService.addFoodEntry(request);
            return ResponseEntity.ok(entryDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Получает сводку дневника за конкретную дату.
     *
     * @param date дата в формате yyyy-MM-dd
     * @param principal текущий пользователь
     * @return сводка по дню или ошибка
     */
    @GetMapping("/day-summary")
    public ResponseEntity<?> getDaySummary(@RequestParam String date, Principal principal) {
        try {
            User user = authUtils.getAuthenticatedUser(principal);
            DaySummaryDTO summary = diaryService.getDaySummary(date, user);
            return ResponseEntity.ok(summary);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Получает список записей о еде для заданного приёма пищи.
     *
     * @param mealId идентификатор приёма пищи
     * @param principal текущий пользователь
     * @return список записей о еде
     */
    @GetMapping("/meals/{mealId}/food-entries")
    public ResponseEntity<?> getFoodEntriesByMeal(@PathVariable Integer mealId, Principal principal) {
        User user = authUtils.getAuthenticatedUser(principal);
        List<FoodEntryDTO> entries = diaryService.getFoodEntriesByMeal(mealId, user);
        return ResponseEntity.ok(entries);
    }

    /**
     * Получает список приёмов пищи с записями о еде за указанную дату.
     *
     * @param date дата в формате yyyy-MM-dd
     * @param principal текущий пользователь
     * @return список приёмов пищи с записями
     */
    @GetMapping("/meals-with-entries")
    public ResponseEntity<?> getMealsWithFoodEntriesByDate(@RequestParam String date, Principal principal) {
        try {
            User user = authUtils.getAuthenticatedUser(principal);
            LocalDate localDate = LocalDate.parse(date); // формат: yyyy-MM-dd
            List<MealWithEntriesResponse> response = diaryService.getMealsWithFoodEntriesByDate(user, localDate);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Получает список приёмов пищи за указанную дату.
     *
     * @param date дата в формате yyyy-MM-dd
     * @param principal текущий пользователь
     * @return список приёмов пищи
     */
    @GetMapping("/meals")
    public ResponseEntity<?> getMealsByDate(@RequestParam String date, Principal principal) {
        try {
            User user = authUtils.getAuthenticatedUser(principal);
            LocalDate localDate = LocalDate.parse(date);
            List<MealDTO> meals = diaryService.getMealsByDate(user, localDate);
            return ResponseEntity.ok(meals);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Обновляет порцию для записи о еде.
     *
     * @param request данные с идентификатором записи и новой порцией в граммах
     * @param principal текущий пользователь
     * @return обновлённая запись о еде
     */
    @PutMapping("/food-entries")
    public ResponseEntity<?> updateFoodEntry(@RequestBody UpdatePortionRequest request, Principal principal) {
        try {
            User user = authUtils.getAuthenticatedUser(principal);
            FoodEntryDTO updated = diaryService.updateFoodEntryPortion(request.entryId(), request.portionGrams(), user);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Удаляет запись о еде по идентификатору.
     *
     * @param entryId идентификатор записи
     * @param principal текущий пользователь
     * @return пустой ответ при успехе
     */
    @DeleteMapping("/food-entries/{entryId}")
    public ResponseEntity<?> deleteFoodEntry(@PathVariable Integer entryId, Principal principal) {
        try {
            User user = authUtils.getAuthenticatedUser(principal);
            diaryService.deleteFoodEntry(entryId, user);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Удаляет приём пищи по идентификатору.
     *
     * @param mealId идентификатор приёма пищи
     * @param principal текущий пользователь
     * @return пустой ответ при успехе
     */
    @DeleteMapping("/meals/{mealId}")
    public ResponseEntity<?> deleteMeal(@PathVariable Integer mealId, Principal principal) {
        try {
            User user = authUtils.getAuthenticatedUser(principal);
            diaryService.deleteMeal(mealId, user);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}