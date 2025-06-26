package ru.naumen.calorietracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.naumen.calorietracker.dto.ExerciseCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseResponse;
import ru.naumen.calorietracker.dto.ExerciseUpdateRequest;
import ru.naumen.calorietracker.model.Exercise;
import ru.naumen.calorietracker.model.elastic.ExerciseSearchDocument;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExerciseMapper {
    ExerciseResponse toExerciseResponse(Exercise exercise);
    Exercise toExercise(ExerciseCreateRequest request);
    Exercise toExercise(ExerciseUpdateRequest request);
    List<ExerciseResponse> toExerciseResponseListFromSearchDocument(List<ExerciseSearchDocument> docs);
    List<ExerciseResponse> toExerciseResponseList(List<Exercise> docs);
    Exercise toExercise(ExerciseResponse response);
} 