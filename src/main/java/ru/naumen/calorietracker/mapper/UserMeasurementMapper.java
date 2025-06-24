package ru.naumen.calorietracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.naumen.calorietracker.dto.UserMeasurementResponse;
import ru.naumen.calorietracker.model.UserMeasurement;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMeasurementMapper {

    @Mapping(source = "user.username", target = "username")
    UserMeasurementResponse toResponse(UserMeasurement measurement);

    List<UserMeasurementResponse> toResponseList(List<UserMeasurement> measurements);
}