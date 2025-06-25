package ru.naumen.calorietracker.service.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.naumen.calorietracker.dto.ExercisePopularityResponse;
import ru.naumen.calorietracker.dto.UserExerciseStatsResponse;
import ru.naumen.calorietracker.repository.ExerciseEntryRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExerciseAnalyticsServiceImplTest {

    private ExerciseEntryRepository exerciseEntryRepository;
    private ExerciseAnalyticsServiceImpl service;

    @BeforeEach
    void setUp() {
        exerciseEntryRepository = mock(ExerciseEntryRepository.class);
        service = new ExerciseAnalyticsServiceImpl(exerciseEntryRepository);
    }

    @Test
    void getTotalExerciseEntries_returnsCorrectCount() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 5);

        when(exerciseEntryRepository.countByDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(42L);

        long count = service.getTotalExerciseEntries(from, to);

        assertEquals(42L, count);
        verify(exerciseEntryRepository).countByDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getPopularExercises_returnsMappedList() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 10);

        when(exerciseEntryRepository.findPopularExercises(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(
                        new Object[]{1, "Running", 10L},
                        new Object[]{2, "Swimming", 5L}
                ));

        List<ExercisePopularityResponse> result = service.getPopularExercises(from, to, 10);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getExerciseId());
        assertEquals("Running", result.get(0).getExerciseName());
        assertEquals(10L, result.get(0).getUsageCount());
    }

    @Test
    void getTotalCaloriesBurned_returnsCorrectSum() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 10);

        when(exerciseEntryRepository.sumCaloriesBurnedBetweenDates(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new BigDecimal("1500.5"));

        BigDecimal totalCalories = service.getTotalCaloriesBurned(from, to);

        assertEquals(new BigDecimal("1500.5"), totalCalories);
    }

    @Test
    void getTotalCaloriesBurned_returnsZeroIfNull() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 10);

        when(exerciseEntryRepository.sumCaloriesBurnedBetweenDates(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(null);

        BigDecimal totalCalories = service.getTotalCaloriesBurned(from, to);

        assertEquals(BigDecimal.ZERO, totalCalories);
    }

    @Test
    void getAverageCaloriesPerExercise_returnsAverage() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 10);

        when(exerciseEntryRepository.sumCaloriesBurnedBetweenDates(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new BigDecimal("1000"));

        when(exerciseEntryRepository.countByDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(4L);

        BigDecimal average = service.getAverageCaloriesPerExercise(from, to);

        assertEquals(new BigDecimal("250.00"), average);
    }

    @Test
    void getAverageCaloriesPerExercise_returnsZeroIfNoData() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 10);

        when(exerciseEntryRepository.sumCaloriesBurnedBetweenDates(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(null);

        when(exerciseEntryRepository.countByDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(0L);

        BigDecimal average = service.getAverageCaloriesPerExercise(from, to);

        assertEquals(BigDecimal.ZERO, average);
    }

    @Test
    void getTopUsersByCaloriesBurned_returnsMappedList() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 10);

        when(exerciseEntryRepository.findTopUsersByCaloriesBurned(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(
                        new Object[]{1, "Alice", 10L, new BigDecimal("100.5")},
                        new Object[]{2, "Bob", 8L, new BigDecimal("80.0")}
                ));

        List<UserExerciseStatsResponse> result = service.getTopUsersByCaloriesBurned(from, to, 10);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getUserId());
        assertEquals("Alice", result.get(0).getUserName());
        assertEquals(10L, result.get(0).getWorkoutCount());
        assertEquals(new BigDecimal("100.5"), result.get(0).getCaloriesBurned());
    }

    @Test
    void getExerciseCountByTimeOfDay_returnsMappedResult() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 10);

        when(exerciseEntryRepository.countExercisesGroupedByTimeOfDay(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(
                        new Object[]{"Morning", 5L},
                        new Object[]{"Afternoon", 10L},
                        new Object[]{"Evening", 3L},
                        new Object[]{"Night", 1L}
                ));

        Map<String, Long> result = service.getExerciseCountByTimeOfDay(from, to);

        assertEquals(4, result.size());
        assertEquals(5L, result.get("Morning"));
        assertEquals(10L, result.get("Afternoon"));
        assertEquals(3L, result.get("Evening"));
        assertEquals(1L, result.get("Night"));
    }
}
