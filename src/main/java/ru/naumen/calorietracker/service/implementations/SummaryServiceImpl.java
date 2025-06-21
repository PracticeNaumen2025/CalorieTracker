package ru.naumen.calorietracker.service.implementations;

import org.springframework.stereotype.Service;
import ru.naumen.calorietracker.dto.DaySummaryRequest;
import ru.naumen.calorietracker.dto.DaySummaryResponse;
import ru.naumen.calorietracker.dto.PeriodSummaryRequest;
import ru.naumen.calorietracker.mapper.DaySummaryMapper;
import ru.naumen.calorietracker.model.DaySummary;
import ru.naumen.calorietracker.model.DaySummaryId;
import ru.naumen.calorietracker.repository.DaySummaryRepository;
import ru.naumen.calorietracker.service.SummaryService;

import java.util.List;

@Service
public class SummaryServiceImpl implements SummaryService {
    private final DaySummaryRepository daySummaryRepository;
    private final DaySummaryMapper daySummaryMapper;

    public SummaryServiceImpl(DaySummaryRepository daySummaryRepository, DaySummaryMapper daySummaryMapper) {
        this.daySummaryRepository = daySummaryRepository;
        this.daySummaryMapper = daySummaryMapper;
    }

    @Override
    public DaySummaryResponse getDaySummary(Integer userId, DaySummaryRequest request) {
        DaySummaryId id = new DaySummaryId();
        id.setUserId(userId);
        id.setDate(request.date());
        DaySummary daySummary = daySummaryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Отчет за дату \"%s\" не найден!".formatted(id.getDate()))
        );
        return daySummaryMapper.toResponse(daySummary);
    }

    @Override
    public List<DaySummaryResponse> getPeriodSummary(Integer userId, PeriodSummaryRequest request) {
        List<DaySummary> daySummaries = daySummaryRepository
                .findAllByUserIdAndDateBetween(userId, request.startPeriod(), request.endPeriod());
        return daySummaryMapper.toResponseList(daySummaries);
    }
}
