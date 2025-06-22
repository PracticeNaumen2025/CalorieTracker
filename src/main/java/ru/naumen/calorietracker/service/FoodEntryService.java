package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.FoodEntryDTO;
import ru.naumen.calorietracker.dto.ProductResponse;
import ru.naumen.calorietracker.model.FoodEntry;
import ru.naumen.calorietracker.model.Meal;
import ru.naumen.calorietracker.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface FoodEntryService {
    FoodEntryDTO addFoodEntry(Meal meal, ProductResponse productResponse, BigDecimal portionGrams);
    List<FoodEntryDTO> getFoodEntriesByMealId(Integer mealId, User user);
    List<FoodEntry> getFoodEntriesByMeals(List<Meal> meals);
    FoodEntryDTO updatePortionGrams(Integer entryId, BigDecimal newPortionGrams, User user);
    void deleteFoodEntry(Integer entryId, User user);
    FoodEntry getFoodEntryById(Integer entryId);
}