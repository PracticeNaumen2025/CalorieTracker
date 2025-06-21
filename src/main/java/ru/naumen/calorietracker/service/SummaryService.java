package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.DaySummaryRequest;
import ru.naumen.calorietracker.dto.DaySummaryResponse;
import ru.naumen.calorietracker.dto.PeriodSummaryRequest;
import java.util.List;

public interface SummaryService {
    DaySummaryResponse getDaySummary(Integer userId, DaySummaryRequest request);
    List<DaySummaryResponse> getPeriodSummary(Integer userId, PeriodSummaryRequest request);
}
