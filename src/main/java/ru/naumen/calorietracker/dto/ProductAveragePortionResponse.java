package ru.naumen.calorietracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductAveragePortionResponse {
    private Integer productId;
    private String productName;
    private Double averagePortionGrams;
}
