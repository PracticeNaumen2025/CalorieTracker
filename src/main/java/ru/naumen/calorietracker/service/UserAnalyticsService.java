package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface UserAnalyticsService {
    UserStatisticsResponse getUserStatistics();
    UserDemographicsResponse getUserDemographics();
    List<UserRegistrationStatsResponse> getRegistrationStats(LocalDate from, LocalDate to);
    ActiveUsersResponse getActiveUsers(int days);
}
