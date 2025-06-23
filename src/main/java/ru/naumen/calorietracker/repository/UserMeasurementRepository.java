package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.model.UserMeasurement;

import java.time.LocalDate;
import java.util.List;

public interface UserMeasurementRepository extends JpaRepository<UserMeasurement, Integer> {
    List<UserMeasurement> findAllByDate(LocalDate date);
    List<UserMeasurement> findAllByUserOrderByDateAsc(User user);
}
