package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.MealDTO;
import ru.naumen.calorietracker.model.Meal;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MealService {
    MealDTO createMeal(User user, LocalDateTime dateTime, String mealType);
    Meal getMealById(Integer mealId);
    List<Meal> getMealsByUserAndDate(User user, LocalDate date);
}
