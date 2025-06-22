package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.DaySummary;
import ru.naumen.calorietracker.model.DaySummaryId;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DaySummaryRepository extends JpaRepository<DaySummary, DaySummaryId> {
    List<DaySummary> findAllByUserIdAndDateBetween(Integer userId,
                                                   LocalDate startDate,
                                                   LocalDate endDate);

    Optional<DaySummary> findByUserIdAndDate(Integer userId, LocalDate date);
}
