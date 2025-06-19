package ru.naumen.calorietracker.mapper;

import org.mapstruct.Mapper;
import ru.naumen.calorietracker.dto.DaySummaryDTO;
import org.mapstruct.MappingConstants;
import ru.naumen.calorietracker.dto.DaySummaryRequest;
import ru.naumen.calorietracker.dto.DaySummaryResponse;
import ru.naumen.calorietracker.model.DaySummary;
import ru.naumen.calorietracker.model.DaySummaryId;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DaySummaryMapper {
    DaySummaryResponse toResponse(DaySummary daySummary);
    DaySummaryId toId(DaySummaryRequest daySummaryRequest);
    List<DaySummaryResponse> toResponseList(List<DaySummary> daySummaries);

    DaySummaryDTO toDTO(DaySummary daySummary);
}
