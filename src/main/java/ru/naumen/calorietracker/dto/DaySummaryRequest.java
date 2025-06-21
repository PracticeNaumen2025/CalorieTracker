package ru.naumen.calorietracker.dto;

import java.time.LocalDate;

public record DaySummaryRequest(
        LocalDate date
) {}
