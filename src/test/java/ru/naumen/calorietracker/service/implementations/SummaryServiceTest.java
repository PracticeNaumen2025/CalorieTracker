package ru.naumen.calorietracker.service.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.naumen.calorietracker.dto.DaySummaryRequest;
import ru.naumen.calorietracker.dto.DaySummaryResponse;
import ru.naumen.calorietracker.dto.PeriodSummaryRequest;
import ru.naumen.calorietracker.mapper.DaySummaryMapper;
import ru.naumen.calorietracker.model.DaySummary;
import ru.naumen.calorietracker.model.DaySummaryId;
import ru.naumen.calorietracker.repository.DaySummaryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SummaryServiceTest {

    @Mock
    private DaySummaryRepository daySummaryRepository;

    @Mock
    private DaySummaryMapper daySummaryMapper;

    @InjectMocks
    private SummaryServiceImpl summaryService;

    private Integer userId;
    private LocalDate date;
    private DaySummaryRequest dayRequest;
    private DaySummaryId summaryId;
    private DaySummary summary;
    private DaySummaryResponse expectedResponse;

    private LocalDate start;
    private LocalDate end;
    private PeriodSummaryRequest periodRequest;
    private List<DaySummary> periodSummaries;
    private List<DaySummaryResponse> expectedResponses;

    @BeforeEach
    void setUp() {
        userId = 1;
        date = LocalDate.of(2025, 6, 21);
        dayRequest = new DaySummaryRequest(userId, date);
        summaryId = new DaySummaryId();
        summaryId.setUserId(userId);
        summaryId.setDate(date);
        summary = new DaySummary();
        expectedResponse = new DaySummaryResponse(date, 100.0, 10.0, 10.0, 10.0);

        start = LocalDate.of(2025, 6, 1);
        end = LocalDate.of(2025, 6, 21);
        periodRequest = new PeriodSummaryRequest(userId, start, end);
        periodSummaries = List.of(new DaySummary(), new DaySummary());
        expectedResponses = List.of(
                new DaySummaryResponse(start, 100.0, 10.0, 10.0, 10.0),
                new DaySummaryResponse(end, 100.0, 11.0, 11.0, 11.0)
        );
    }

    @Test
    void getDaySummary_ShouldReturnDaySummary() {
        when(daySummaryMapper.toId(dayRequest)).thenReturn(summaryId);
        when(daySummaryRepository.findById(summaryId)).thenReturn(Optional.of(summary));
        when(daySummaryMapper.toResponse(summary)).thenReturn(expectedResponse);

        DaySummaryResponse actual = summaryService.getDaySummary(dayRequest);

        assertEquals(expectedResponse, actual);
        verify(daySummaryMapper).toResponse(summary);
        verify(daySummaryRepository).findById(summaryId);
    }

    @Test
    void getDaySummary_ShouldThrowException_WhenNotFound() {
        when(daySummaryMapper.toId(dayRequest)).thenReturn(summaryId);
        when(daySummaryRepository.findById(summaryId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> summaryService.getDaySummary(dayRequest));
        assertEquals("Отчет за дату \"2025-06-21\" не найден!", ex.getMessage());
    }

    @Test
    void getPeriodSummary_ShouldReturnListOfSummaries() {
        when(daySummaryRepository.findAllByUserIdAndDateBetween(userId, start, end))
                .thenReturn(periodSummaries);
        when(daySummaryMapper.toResponseList(periodSummaries)).thenReturn(expectedResponses);

        List<DaySummaryResponse> result = summaryService.getPeriodSummary(periodRequest);

        assertEquals(expectedResponses, result);
        verify(daySummaryRepository).findAllByUserIdAndDateBetween(userId, start, end);
    }
}