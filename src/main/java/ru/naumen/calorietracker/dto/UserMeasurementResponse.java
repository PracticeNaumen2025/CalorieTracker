package ru.naumen.calorietracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserMeasurementResponse(
        Integer measurementId,
        String username,
        LocalDate date,
        BigDecimal weightKg,
        BigDecimal heightCm
) {}
