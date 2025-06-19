package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.Meal;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Integer> {
    List<Meal> findAllByUserAndDateTimeBetween(User user, LocalDateTime start, LocalDateTime end);
}
