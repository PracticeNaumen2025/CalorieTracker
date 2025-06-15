package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.Meal;

public interface MealRepository extends JpaRepository<Meal, Integer> {
}
