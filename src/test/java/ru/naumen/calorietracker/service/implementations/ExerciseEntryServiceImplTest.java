package ru.naumen.calorietracker.service.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseEntryServiceImplTest {

    @Mock
    private ExerciseEntryRepository exerciseEntryRepository;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private ExerciseEntryMapper exerciseEntryMapper;
    @Mock
    private AccessChecker accessChecker;

    @InjectMocks
    private ExerciseEntryServiceImpl exerciseEntryService;

    private User user;
    private Exercise exercise;
    private ExerciseEntry exerciseEntry;
    private ExerciseEntryResponse exerciseEntryResponse;
    private ExerciseEntryCreateRequest createRequest;
    private ExerciseEntryUpdateRequest updateRequest;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        user = new User();
        user.setUserId(1);
        user.setUsername("testuser");

        exercise = new Exercise();
        exercise.setExerciseId(1);
        exercise.setName("Бег");
        exercise.setCaloriesPerHour(new BigDecimal("600"));

        exerciseEntry = new ExerciseEntry();
        exerciseEntry.setEntryId(1);
        exerciseEntry.setUser(user);
        exerciseEntry.setExercise(exercise);
        exerciseEntry.setDurationMinutes(30);
        exerciseEntry.setDateTime(now);
        exerciseEntry.setCaloriesBurned(new BigDecimal("300"));

        exerciseEntryResponse = new ExerciseEntryResponse();
        exerciseEntryResponse.setEntryId(1);
        exerciseEntryResponse.setExerciseId(1);
        exerciseEntryResponse.setDurationMinutes(30);
        exerciseEntryResponse.setDateTime(now);

        createRequest = new ExerciseEntryCreateRequest();
        createRequest.setExerciseId(1);
        createRequest.setDurationMinutes(30);
        createRequest.setDateTime(now);

        updateRequest = new ExerciseEntryUpdateRequest();
        updateRequest.setEntryId(1);
        updateRequest.setExerciseId(1);
        updateRequest.setDurationMinutes(45);
        updateRequest.setDateTime(now.plusHours(1));
    }

    /**
     * Проверяет успешное получение записи о тренировке по id,
     * когда запись существует и у пользователя есть доступ.
     */
    @Test
    void getExerciseEntryById_ShouldReturnEntry_WhenFoundAndAccessGranted() {
        when(exerciseEntryRepository.findById(1)).thenReturn(Optional.of(exerciseEntry));
        doNothing().when(accessChecker).checkAccess(user.getUserId(), user.getUserId());
        when(exerciseEntryMapper.toResponse(exerciseEntry)).thenReturn(exerciseEntryResponse);

        ExerciseEntryResponse result = exerciseEntryService.getExerciseEntryById(1, user);

        assertNotNull(result);
        assertEquals(exerciseEntryResponse.getEntryId(), result.getEntryId());
        verify(exerciseEntryRepository).findById(1);
        verify(accessChecker).checkAccess(user.getUserId(), user.getUserId());
        verify(exerciseEntryMapper).toResponse(exerciseEntry);
    }

    /**
     * Проверяет выброс исключения EntityNotFoundException,
     * когда запись о тренировке по id не найдена.
     */
    @Test
    void getExerciseEntryById_ShouldThrowException_WhenEntryNotFound() {
        when(exerciseEntryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> exerciseEntryService.getExerciseEntryById(1, user));
        verify(exerciseEntryRepository).findById(1);
        verifyNoInteractions(accessChecker, exerciseEntryMapper);
    }

    /**
     * Проверяет выброс исключения при попытке получения чужой записи,
     * когда AccessChecker блокирует доступ.
     */
    @Test
    void getExerciseEntryById_ShouldThrowException_WhenAccessDenied() {
        when(exerciseEntryRepository.findById(1)).thenReturn(Optional.of(exerciseEntry));
        doThrow(new RuntimeException("Access Denied")).when(accessChecker).checkAccess(user.getUserId(), user.getUserId());

        assertThrows(RuntimeException.class, () -> exerciseEntryService.getExerciseEntryById(1, user));
        verify(exerciseEntryRepository).findById(1);
        verify(accessChecker).checkAccess(user.getUserId(), user.getUserId());
        verifyNoInteractions(exerciseEntryMapper);
    }

    /**
     * Проверяет возврат отфильтрованного списка записей о тренировках
     * для указанного пользователя и временного диапазона.
     */
    @Test
    void getExerciseEntriesByDate_ShouldReturnFilteredEntries() {
        User anotherUser = new User();
        anotherUser.setUserId(2);

        ExerciseEntry otherUserEntry = new ExerciseEntry();
        otherUserEntry.setUser(anotherUser);
        otherUserEntry.setDateTime(now);

        ExerciseEntry outOfDateEntry = new ExerciseEntry();
        outOfDateEntry.setUser(user);
        outOfDateEntry.setDateTime(now.minusDays(1));

        List<ExerciseEntry> allEntries = List.of(exerciseEntry, otherUserEntry, outOfDateEntry);

        when(exerciseEntryRepository.findAll()).thenReturn(allEntries);
        when(exerciseEntryMapper.toResponse(exerciseEntry)).thenReturn(exerciseEntryResponse);

        LocalDateTime start = now.minusHours(1);
        LocalDateTime end = now.plusHours(1);

        List<ExerciseEntryResponse> result = exerciseEntryService.getExerciseEntriesByDate(start, end, user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(exerciseEntryResponse, result.get(0));
        verify(exerciseEntryRepository).findAll();
        verify(exerciseEntryMapper, times(1)).toResponse(exerciseEntry);
    }

    /**
     * Проверяет возврат пустого списка, когда нет записей в указанном
     * временном диапазоне.
     */
    @Test
    void getExerciseEntriesByDate_ShouldReturnEmptyList_WhenNoEntriesMatch() {
        when(exerciseEntryRepository.findAll()).thenReturn(Collections.emptyList());

        LocalDateTime start = now.minusHours(1);
        LocalDateTime end = now.plusHours(1);

        List<ExerciseEntryResponse> result = exerciseEntryService.getExerciseEntriesByDate(start, end, user);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(exerciseEntryRepository).findAll();
        verifyNoInteractions(exerciseEntryMapper);
    }

    /**
     * Проверяет успешное создание новой записи о тренировке.
     */
    @Test
    void createExerciseEntry_ShouldCreateAndReturnEntry() {
        when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise));
        when(exerciseEntryMapper.toExerciseEntry(createRequest)).thenReturn(exerciseEntry);
        when(exerciseEntryRepository.save(any(ExerciseEntry.class))).thenReturn(exerciseEntry);
        when(exerciseEntryMapper.toResponse(exerciseEntry)).thenReturn(exerciseEntryResponse);

        ExerciseEntryResponse result = exerciseEntryService.createExerciseEntry(createRequest, user);

        assertNotNull(result);
        assertEquals(exerciseEntryResponse, result);

        verify(exerciseRepository).findById(1);
        verify(exerciseEntryMapper).toExerciseEntry(createRequest);
        verify(exerciseEntryRepository).save(exerciseEntry);
        verify(exerciseEntryMapper).toResponse(exerciseEntry);

        assertEquals(user, exerciseEntry.getUser());
        assertEquals(exercise, exerciseEntry.getExercise());
        // 30 мин / 60.0 * 600 ккал/час = 300
        assertEquals(0, new BigDecimal("300.0").compareTo(exerciseEntry.getCaloriesBurned()));
    }

    /**
     * Проверяет выброс исключения EntityNotFoundException при создании записи,
     * если указанное упражнение не существует.
     */
    @Test
    void createExerciseEntry_ShouldThrowException_WhenExerciseNotFound() {
        when(exerciseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> exerciseEntryService.createExerciseEntry(createRequest, user));
        verify(exerciseRepository).findById(1);
        verifyNoInteractions(exerciseEntryMapper, exerciseEntryRepository);
    }

    /**
     * Проверяет успешное обновление существующей записи о тренировке.
     */
    @Test
    void updateExerciseEntry_ShouldUpdateAndReturnEntry() {
        when(exerciseEntryRepository.findById(1)).thenReturn(Optional.of(exerciseEntry));
        doNothing().when(accessChecker).checkAccess(user.getUserId(), user.getUserId());
        when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise));
        when(exerciseEntryRepository.save(any(ExerciseEntry.class))).thenReturn(exerciseEntry);
        when(exerciseEntryMapper.toResponse(exerciseEntry)).thenReturn(exerciseEntryResponse);

        ExerciseEntryResponse result = exerciseEntryService.updateExerciseEntry(updateRequest, user);

        assertNotNull(result);
        assertEquals(exerciseEntryResponse, result);

        verify(exerciseEntryRepository).findById(1);
        verify(accessChecker).checkAccess(user.getUserId(), user.getUserId());
        verify(exerciseRepository).findById(1);
        verify(exerciseEntryRepository).save(exerciseEntry);
        verify(exerciseEntryMapper).toResponse(exerciseEntry);

        assertEquals(updateRequest.getDurationMinutes(), exerciseEntry.getDurationMinutes());
        assertEquals(updateRequest.getDateTime(), exerciseEntry.getDateTime());
        // 45 мин / 60.0 * 600 ккал/час = 450
        assertEquals(0, new BigDecimal("450.0").compareTo(exerciseEntry.getCaloriesBurned()));
    }

    /**
     * Проверяет выброс исключения EntityNotFoundException при обновлении,
     * если запись о тренировке не найдена.
     */
    @Test
    void updateExerciseEntry_ShouldThrowException_WhenEntryNotFound() {
        when(exerciseEntryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> exerciseEntryService.updateExerciseEntry(updateRequest, user));
        verify(exerciseEntryRepository).findById(1);
        verifyNoInteractions(accessChecker, exerciseRepository, exerciseEntryMapper);
    }

    /**
     * Проверяет выброс исключения при обновлении,
     * если у пользователя нет прав доступа.
     */
    @Test
    void updateExerciseEntry_ShouldThrowException_WhenAccessDenied() {
        when(exerciseEntryRepository.findById(1)).thenReturn(Optional.of(exerciseEntry));
        doThrow(new RuntimeException("Access Denied")).when(accessChecker).checkAccess(user.getUserId(), user.getUserId());

        assertThrows(RuntimeException.class, () -> exerciseEntryService.updateExerciseEntry(updateRequest, user));
        verify(exerciseEntryRepository).findById(1);
        verify(accessChecker).checkAccess(user.getUserId(), user.getUserId());
        verifyNoInteractions(exerciseRepository, exerciseEntryMapper);
    }

    /**
     * Проверяет выброс исключения EntityNotFoundException при обновлении,
     * если новое указанное упражнение не найдено.
     */
    @Test
    void updateExerciseEntry_ShouldThrowException_WhenExerciseNotFound() {
        when(exerciseEntryRepository.findById(1)).thenReturn(Optional.of(exerciseEntry));
        doNothing().when(accessChecker).checkAccess(user.getUserId(), user.getUserId());
        when(exerciseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> exerciseEntryService.updateExerciseEntry(updateRequest, user));
        verify(exerciseEntryRepository).findById(1);
        verify(accessChecker).checkAccess(user.getUserId(), user.getUserId());
        verify(exerciseRepository).findById(1);
        verifyNoMoreInteractions(exerciseEntryRepository);
        verifyNoInteractions(exerciseEntryMapper);
    }
} 