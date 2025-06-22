package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.naumen.calorietracker.dto.DaySummaryDTO;
import ru.naumen.calorietracker.mapper.DaySummaryMapper;
import ru.naumen.calorietracker.model.DaySummary;
import ru.naumen.calorietracker.model.FoodEntry;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.repository.DaySummaryRepository;
import ru.naumen.calorietracker.repository.FoodEntryRepository;
import ru.naumen.calorietracker.service.DaySummaryService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DaySummaryServiceImpl implements DaySummaryService {
    private final DaySummaryRepository daySummaryRepository;
    private final DaySummaryMapper daySummaryMapper;

    @Override
    public DaySummaryDTO getSummaryByUserAndDate(User user, LocalDate date) {
        DaySummary summary = daySummaryRepository.findByUserIdAndDate(user.getUserId(), date)
                .orElseThrow(() -> new RuntimeException("Нет данных на эту дату"));
        return daySummaryMapper.toDTO(summary);
    }
}
