package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.ExerciseEntry;

public interface ExerciseEntryRepository extends JpaRepository<ExerciseEntry, Integer> {
}
