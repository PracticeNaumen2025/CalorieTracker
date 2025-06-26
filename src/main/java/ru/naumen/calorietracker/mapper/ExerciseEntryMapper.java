package ru.naumen.calorietracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.naumen.calorietracker.dto.ExerciseEntryCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseEntryResponse;
import ru.naumen.calorietracker.model.ExerciseEntry;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciseEntryMapper {
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "exerciseId", source = "exercise.exerciseId")
    ExerciseEntryResponse toResponse(ExerciseEntry entry);

    List<ExerciseEntryResponse> toResponseList(List<ExerciseEntry> entries);
    ExerciseEntry toExerciseEntry(ExerciseEntryCreateRequest createRequest);
} 