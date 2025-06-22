package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.UserGoalCreateRequest;
import ru.naumen.calorietracker.dto.UserGoalResponse;
import ru.naumen.calorietracker.dto.UserGoalUpdateRequest;

import java.time.LocalDate;
import java.util.List;

public interface UserGoalService {
    UserGoalResponse createUserGoal(UserGoalCreateRequest request, Integer userId);
    UserGoalResponse updateUserGoal(UserGoalUpdateRequest request, Integer userId);
    UserGoalResponse getUserGoalByUserIdAndDate(Integer ownerUserId, Integer currentUserId, LocalDate date);
    List<UserGoalResponse> getUserGoalsByUserId(Integer ownerUserId, Integer currentUserId);
    void deleteUserGoalByUserId(Integer userId, Integer goalId);
}
