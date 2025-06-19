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

@RestController
@RequestMapping("/api/diary")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DiaryController {
    private final AuthUtils authUtils;
    private final DiaryService diaryService;

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

    @PostMapping("/food-entries")
    public ResponseEntity<?> addFoodEntry(@RequestBody AddFoodEntryRequest request) {
        try {
            FoodEntryDTO entryDTO = diaryService.addFoodEntry(request);
            return ResponseEntity.ok(entryDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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

    @GetMapping("/meals/{mealId}/food-entries")
    public ResponseEntity<?> getFoodEntriesByMeal(@PathVariable Integer mealId, Principal principal) {
        User user = authUtils.getAuthenticatedUser(principal);
        List<FoodEntryDTO> entries = diaryService.getFoodEntriesByMeal(mealId, user);
        return ResponseEntity.ok(entries);
    }

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
}