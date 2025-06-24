package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.UserMeasurementResponse;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserMeasurementService {
    void saveDailyUserMeasurements(LocalDate date);
    List<UserMeasurementResponse> getAllMeasurementsByUser(User user);
}
