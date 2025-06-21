package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.DaySummary;
import ru.naumen.calorietracker.model.DaySummaryId;

import java.time.LocalDate;
import java.util.List;

public interface DaySummaryRepository extends JpaRepository<DaySummary, DaySummaryId> {
    List<DaySummary> findAllByUserIdAndDateBetween(Integer userId,
                                                   LocalDate startDate,
                                                   LocalDate endDate);
}
