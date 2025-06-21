package ru.naumen.calorietracker.dto;

import java.time.LocalDate;

public record DaySummaryRequest(
        Integer userId,
        LocalDate date
) {}
