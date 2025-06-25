package ru.naumen.calorietracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryPopularityResponse{
    private String categoryName;
    private Long usageCount;
}
