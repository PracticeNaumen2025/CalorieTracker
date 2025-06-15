package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.UserGoal;

public interface UserGoalRepository extends JpaRepository<UserGoal, Integer> {
}
