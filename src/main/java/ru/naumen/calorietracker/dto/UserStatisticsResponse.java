package ru.naumen.calorietracker.dto;

import lombok.Data;
import java.util.Map;

@Data
public class UserStatisticsResponse {
    private long totalUsers;
    private long activeUsers;
    private long deletedUsers;
    private Map<String, Long> usersByRole;
}
