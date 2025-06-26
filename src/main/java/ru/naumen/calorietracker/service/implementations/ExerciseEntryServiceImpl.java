package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.naumen.calorietracker.dto.ExerciseEntryCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseEntryResponse;
import ru.naumen.calorietracker.dto.ExerciseEntryUpdateRequest;
import ru.naumen.calorietracker.exception.EntityNotFoundException;
import ru.naumen.calorietracker.mapper.ExerciseEntryMapper;
import ru.naumen.calorietracker.model.Exercise;
import ru.naumen.calorietracker.model.ExerciseEntry;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.repository.ExerciseEntryRepository;
import ru.naumen.calorietracker.repository.ExerciseRepository;
import ru.naumen.calorietracker.service.ExerciseEntryService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseEntryServiceImpl implements ExerciseEntryService {
    private final ExerciseEntryRepository exerciseEntryRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseEntryMapper exerciseEntryMapper;
    private final AccessChecker accessChecker;

    @Override
    public ExerciseEntryResponse getExerciseEntryById(Integer id, User user) {
        ExerciseEntry entry = exerciseEntryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись о физической активности не найдена"));
        accessChecker.checkAccess(entry.getUser().getUserId(), user.getUserId());

        return exerciseEntryMapper.toResponse(entry);
    }

    @Override
    public List<ExerciseEntryResponse> getExerciseEntriesByDate(LocalDateTime start, LocalDateTime end, User user) {
        List<ExerciseEntry> entries = exerciseEntryRepository.findAll().stream()
                .filter(e -> e.getUser().getUserId().equals(user.getUserId()) &&
                        !e.getDateTime().isBefore(start) && !e.getDateTime().isAfter(end))
                .toList();
        return entries.stream().map(exerciseEntryMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ExerciseEntryResponse createExerciseEntry(ExerciseEntryCreateRequest request, User user) {
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new EntityNotFoundException("Упражнение не найдено"));
        ExerciseEntry entry = exerciseEntryMapper.toExerciseEntry(request);

        entry.setExercise(exercise);
        entry.setUser(user);
        entry.setCaloriesBurned(BigDecimal.valueOf(request.getDurationMinutes()/60.0).multiply(exercise.getCaloriesPerHour()));
        ExerciseEntry saved = exerciseEntryRepository.save(entry);
        return exerciseEntryMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ExerciseEntryResponse updateExerciseEntry(ExerciseEntryUpdateRequest request, User user) {
        ExerciseEntry entry = exerciseEntryRepository.findById(request.getEntryId())
                .orElseThrow(() -> new EntityNotFoundException("Запись о физической активности не найдена"));
        accessChecker.checkAccess(entry.getUser().getUserId(), user.getUserId());
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new EntityNotFoundException("Упражнение не найдено"));
        entry.setExercise(exercise);
        entry.setDateTime(request.getDateTime());
        entry.setDurationMinutes(request.getDurationMinutes());
        entry.setCaloriesBurned(BigDecimal.valueOf(request.getDurationMinutes()/60.0).multiply(exercise.getCaloriesPerHour()));
        ExerciseEntry saved = exerciseEntryRepository.save(entry);
        return exerciseEntryMapper.toResponse(saved);
    }
} 