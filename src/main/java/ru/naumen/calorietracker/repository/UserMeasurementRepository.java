package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.UserMeasurement;

public interface UserMeasurementRepository extends JpaRepository<UserMeasurement, Integer> {
}
