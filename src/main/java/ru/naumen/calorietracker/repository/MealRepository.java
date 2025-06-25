package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.naumen.calorietracker.model.Meal;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Integer> {
    List<Meal> findAllByUserAndDateTimeBetween(User user, LocalDateTime start, LocalDateTime end);
    @Query("SELECT DISTINCT m.user.userId FROM Meal m WHERE m.dateTime >= :since")
    List<Integer> findActiveUserIdsSince(@Param("since") LocalDateTime since);
}
