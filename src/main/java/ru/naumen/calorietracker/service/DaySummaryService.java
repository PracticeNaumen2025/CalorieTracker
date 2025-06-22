package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.DaySummaryDTO;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDate;

public interface DaySummaryService {
    DaySummaryDTO getSummaryByUserAndDate(User user, LocalDate date);
}
