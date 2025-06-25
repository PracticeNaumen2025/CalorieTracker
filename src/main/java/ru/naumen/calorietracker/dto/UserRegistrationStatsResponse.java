package ru.naumen.calorietracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserRegistrationStatsResponse {
    private LocalDate date;
    private long count;
}
