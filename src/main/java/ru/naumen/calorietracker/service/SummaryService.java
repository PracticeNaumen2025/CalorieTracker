package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.DaySummaryRequest;
import ru.naumen.calorietracker.dto.DaySummaryResponse;
import ru.naumen.calorietracker.dto.PeriodSummaryRequest;
import java.util.List;

public interface SummaryService {
    DaySummaryResponse getDaySummary(DaySummaryRequest request);
    List<DaySummaryResponse> getPeriodSummary(PeriodSummaryRequest request);
}
