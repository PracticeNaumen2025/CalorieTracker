package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.*;
import ru.naumen.calorietracker.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DiaryService {
    MealDTO createMeal(CreateMealRequest request, User user);
    FoodEntryDTO addFoodEntry(AddFoodEntryRequest request);
    DaySummaryDTO getDaySummary(String date, User user);
    List<FoodEntryDTO> getFoodEntriesByMeal(Integer mealId, User user);
    List<MealWithEntriesResponse> getMealsWithFoodEntriesByDate(User user, LocalDate date);
    List<MealDTO> getMealsByDate(User user, LocalDate date);
    FoodEntryDTO updateFoodEntryPortion(Integer entryId, BigDecimal newPortionGrams, User user);
    void deleteFoodEntry(Integer entryId, User user);
    void deleteMeal(Integer mealId, User user);
}
