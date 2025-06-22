package ru.naumen.calorietracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.naumen.calorietracker.dto.UserGoalCreateRequest;
import ru.naumen.calorietracker.dto.UserGoalResponse;
import ru.naumen.calorietracker.model.UserGoal;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserGoalMapper {

    UserGoal toEntity(UserGoalCreateRequest createRequest);
    UserGoalResponse toResponse(UserGoal userGoal);
    List<UserGoalResponse> toResponseList(List<UserGoal> goals);
}
