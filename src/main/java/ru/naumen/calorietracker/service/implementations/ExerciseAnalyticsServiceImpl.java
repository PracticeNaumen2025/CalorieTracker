package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.naumen.calorietracker.dto.ExercisePopularityResponse;
import ru.naumen.calorietracker.dto.UserExerciseStatsResponse;
import ru.naumen.calorietracker.repository.ExerciseEntryRepository;
import ru.naumen.calorietracker.service.ExerciseAnalyticsService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseAnalyticsServiceImpl implements ExerciseAnalyticsService {

    private final ExerciseEntryRepository exerciseEntryRepository;

    @Override
    public long getTotalExerciseEntries(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);
        return exerciseEntryRepository.countByDateTimeBetween(start, end);
    }

    @Override
    public List<ExercisePopularityResponse> getPopularExercises(LocalDate from, LocalDate to, int limit) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);

        List<Object[]> rows = exerciseEntryRepository.findPopularExercises(start, end);
        return rows.stream()
                .map(r -> new ExercisePopularityResponse((Integer) r[0], (String) r[1], (Long) r[2]))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalCaloriesBurned(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);
        BigDecimal total = exerciseEntryRepository.sumCaloriesBurnedBetweenDates(start, end);
        return total == null ? BigDecimal.ZERO : total;
    }

    @Override
    public BigDecimal getAverageCaloriesPerExercise(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);

        BigDecimal totalCalories = exerciseEntryRepository.sumCaloriesBurnedBetweenDates(start, end);
        long count = exerciseEntryRepository.countByDateTimeBetween(start, end);

        if (totalCalories == null || count == 0) {
            return BigDecimal.ZERO;
        }
        return totalCalories.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }

    @Override
    public List<UserExerciseStatsResponse> getTopUsersByCaloriesBurned(LocalDate from, LocalDate to, int limit) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);

        List<Object[]> rows = exerciseEntryRepository.findTopUsersByCaloriesBurned(start, end);
        return rows.stream()
                .map(r -> new UserExerciseStatsResponse(
                        (Integer) r[0],
                        (String) r[1],
                        (Long) r[2],
                        (BigDecimal) r[3]
                ))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getExerciseCountByTimeOfDay(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);

        List<Object[]> rows = exerciseEntryRepository.countExercisesGroupedByTimeOfDay(start, end);
        return rows.stream()
                .collect(Collectors.toMap(
                        r -> (String) r[0],
                        r -> (Long) r[1]
                ));
    }
}

