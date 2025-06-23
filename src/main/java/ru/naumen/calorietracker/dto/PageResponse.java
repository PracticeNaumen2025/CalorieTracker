package ru.naumen.calorietracker.dto;

import java.io.Serializable;
import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean first,
        boolean last,
        int numberOfElements
) implements Serializable {}
