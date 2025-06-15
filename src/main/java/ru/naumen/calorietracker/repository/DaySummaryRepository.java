package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.DaySummary;

public interface DaySummaryRepository extends JpaRepository<DaySummary, Integer> {
}
