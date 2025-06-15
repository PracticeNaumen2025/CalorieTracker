package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
}
