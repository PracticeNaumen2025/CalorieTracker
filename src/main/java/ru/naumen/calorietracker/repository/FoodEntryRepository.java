package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.FoodEntry;

public interface FoodEntryRepository extends JpaRepository<FoodEntry, Integer> {
}
