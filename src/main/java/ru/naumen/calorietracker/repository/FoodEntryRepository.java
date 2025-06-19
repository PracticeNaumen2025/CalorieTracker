package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.FoodEntry;
import ru.naumen.calorietracker.model.Meal;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface FoodEntryRepository extends JpaRepository<FoodEntry, Integer> {
    List<FoodEntry> findByMealUserAndMealDateTimeBetween(User user, LocalDateTime start, LocalDateTime end);
    List<FoodEntry> findByMealMealId(Integer mealId);
    List<FoodEntry> findByMealIn(List<Meal> meals);
}
