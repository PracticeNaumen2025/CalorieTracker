package ru.naumen.calorietracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPopularityResponse {
    private Integer productId;
    private String productName;
    private Long usageCount;
}

