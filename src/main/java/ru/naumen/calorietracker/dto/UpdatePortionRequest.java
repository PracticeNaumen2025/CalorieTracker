package ru.naumen.calorietracker.dto;

import java.math.BigDecimal;

public record UpdatePortionRequest(Integer entryId, BigDecimal portionGrams) {}
