package ru.naumen.calorietracker.dto;

import java.time.LocalDate;

public record PeriodSummaryRequest(
        LocalDate startPeriod,
        LocalDate endPeriod
) {}
