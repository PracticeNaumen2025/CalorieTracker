package ru.naumen.calorietracker.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class UserDemographicsResponse {
    private Map<String, Long> genderDistribution;
    private double averageAge;
    private Map<String, Long> ageGroups;
    private BigDecimal averageHeightCm;
    private BigDecimal averageWeightKg;
}
