package ru.naumen.calorietracker.service.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.naumen.calorietracker.dto.ExerciseCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseResponse;
import ru.naumen.calorietracker.dto.ExerciseUpdateRequest;
import ru.naumen.calorietracker.mapper.ExerciseMapper;
import ru.naumen.calorietracker.model.Exercise;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.model.elastic.ExerciseSearchDocument;
import ru.naumen.calorietracker.repository.ExerciseRepository;
import ru.naumen.calorietracker.repository.search.ExerciseSearchRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceImplTest {
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private ExerciseSearchRepository exerciseSearchRepository;
    @Mock
    private ExerciseMapper exerciseMapper;
    @Mock
    private AccessChecker accessChecker;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    private User user;
    private Exercise exercise;
    private ExerciseResponse exerciseResponse;
    private ExerciseCreateRequest createRequest;
    private ExerciseUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);
        user.setUsername("testuser");

        exercise = new Exercise();
        exercise.setExerciseId(1);
        exercise.setName("Бег");
        exercise.setCaloriesPerHour(new BigDecimal("500"));
        exercise.setCreator(user);

        exerciseResponse = new ExerciseResponse();
        exerciseResponse.setExerciseId(1);
        exerciseResponse.setName("Бег");
        exerciseResponse.setCaloriesPerHour(500);

        createRequest = new ExerciseCreateRequest();
        createRequest.setName("Бег");
        createRequest.setCaloriesPerHour(new BigDecimal("500"));

        updateRequest = new ExerciseUpdateRequest();
        updateRequest.setExerciseId(1);
        updateRequest.setName("Бег быстрый");
        updateRequest.setCaloriesPerHour(new BigDecimal("600"));
    }

    /**
     * Проверяет возврат страницы упражнений при наличии данных.
     */
    @Test
    void getAllExercises_ShouldReturnPage() {
        when(exerciseRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(exercise)));
        when(exerciseMapper.toExerciseResponseList(List.of(exercise))).thenReturn(List.of(exerciseResponse));

        Page<ExerciseResponse> result = exerciseService.getAllExercises(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Бег", result.getContent().get(0).getName());
        verify(exerciseRepository).findAll(pageable);
        verify(exerciseMapper).toExerciseResponseList(List.of(exercise));
    }

    /**
     * Проверяет возврат пустой страницы упражнений.
     */
    @Test
    void getAllExercises_ShouldReturnEmptyPage() {
        when(exerciseRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(exerciseMapper.toExerciseResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

        Page<ExerciseResponse> result = exerciseService.getAllExercises(pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(exerciseRepository).findAll(pageable);
        verify(exerciseMapper).toExerciseResponseList(Collections.emptyList());
    }

    /**
     * Проверяет успешное получение упражнения по id.
     */
    @Test
    void getExerciseById_ShouldReturnExercise() {
        when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise));
        when(exerciseMapper.toExerciseResponse(exercise)).thenReturn(exerciseResponse);

        ExerciseResponse result = exerciseService.getExerciseById(1);

        assertNotNull(result);
        assertEquals(1, result.getExerciseId());
        verify(exerciseRepository).findById(1);
        verify(exerciseMapper).toExerciseResponse(exercise);
    }

    /**
     * Проверяет выброс исключения при отсутствии упражнения по id.
     */
    @Test
    void getExerciseById_ShouldThrowException_WhenNotFound() {
        when(exerciseRepository.findById(1)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> exerciseService.getExerciseById(1));
        assertTrue(ex.getMessage().contains("не найдено"));
        verify(exerciseRepository).findById(1);
        verifyNoInteractions(exerciseMapper);
    }

    /**
     * Проверяет успешное создание упражнения.
     */
    @Test
    void createExercise_ShouldCreateExercise() {
        when(exerciseMapper.toExercise(createRequest)).thenReturn(exercise);
        when(exerciseRepository.save(exercise)).thenReturn(exercise);
        when(exerciseMapper.toExerciseResponse(exercise)).thenReturn(exerciseResponse);

        ExerciseResponse result = exerciseService.createExercise(createRequest, user);

        assertNotNull(result);
        assertEquals("Бег", result.getName());
        assertEquals(user, exercise.getCreator());
        verify(exerciseMapper).toExercise(createRequest);
        verify(exerciseRepository).save(exercise);
        verify(exerciseMapper).toExerciseResponse(exercise);
    }

    /**
     * Проверяет успешное обновление упражнения владельцем.
     */
    @Test
    void updateExercise_ShouldUpdate_WhenOwner() {
        when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise));
        doNothing().when(accessChecker).checkAccess(user.getUserId(), user.getUserId());
        Exercise updated = new Exercise();
        updated.setName("Бег быстрый");
        updated.setCaloriesPerHour(new BigDecimal("600"));
        when(exerciseMapper.toExercise(updateRequest)).thenReturn(updated);
        when(exerciseRepository.save(exercise)).thenReturn(exercise);
        when(exerciseMapper.toExerciseResponse(exercise)).thenReturn(exerciseResponse);

        ExerciseResponse result = exerciseService.updateExercise(updateRequest, user);

        assertNotNull(result);
        verify(exerciseRepository).findById(1);
        verify(accessChecker).checkAccess(user.getUserId(), user.getUserId());
        verify(exerciseMapper).toExercise(updateRequest);
        verify(exerciseRepository).save(exercise);
        verify(exerciseMapper).toExerciseResponse(exercise);
        assertEquals("Бег быстрый", exercise.getName());
        assertEquals(new BigDecimal("600"), exercise.getCaloriesPerHour());
    }

    /**
     * Проверяет выброс исключения при отсутствии доступа к обновлению.
     */
    @Test
    void updateExercise_ShouldThrowException_WhenNoAccess() {
        when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise));
        doThrow(new RuntimeException("Нет доступа")).when(accessChecker).checkAccess(anyInt(), anyInt());
        updateRequest.setExerciseId(1);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> exerciseService.updateExercise(updateRequest, user));
        assertTrue(ex.getMessage().contains("Нет доступа"));
        verify(exerciseRepository).findById(1);
        verify(accessChecker).checkAccess(anyInt(), anyInt());
        verifyNoMoreInteractions(exerciseMapper, exerciseRepository);
    }

    /**
     * Проверяет выброс исключения при отсутствии упражнения для обновления.
     */
    @Test
    void updateExercise_ShouldThrowException_WhenNotFound() {
        when(exerciseRepository.findById(1)).thenReturn(Optional.empty());
        updateRequest.setExerciseId(1);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> exerciseService.updateExercise(updateRequest, user));
        assertTrue(ex.getMessage().contains("не найдено"));
        verify(exerciseRepository).findById(1);
        verifyNoInteractions(accessChecker, exerciseMapper);
    }

    /**
     * Проверяет успешное удаление упражнения по id.
     */
    @Test
    void deleteExercise_ShouldDelete() {
        doNothing().when(exerciseRepository).deleteById(1);
        exerciseService.deleteExercise(1);
        verify(exerciseRepository).deleteById(1);
    }

    /**
     * Проверяет успешный fuzzy-поиск по имени.
     */
    @Test
    void searchByName_ShouldReturnFuzzyResults() {
        ExerciseSearchDocument doc = new ExerciseSearchDocument(1, "Бег", new BigDecimal("500"), 1);
        List<ExerciseSearchDocument> docs = List.of(doc);
        ExerciseResponse resp = new ExerciseResponse();
        resp.setExerciseId(1);
        resp.setName("Бег");
        resp.setCaloriesPerHour(500);
        when(exerciseSearchRepository.findByNameFuzzy("Бег")).thenReturn(docs);
        when(exerciseMapper.toExerciseResponseListFromSearchDocument(docs)).thenReturn(List.of(resp));
        when(pageable.getOffset()).thenReturn(0L);
        when(pageable.getPageSize()).thenReturn(10);

        Page<ExerciseResponse> result = exerciseService.searchByName("Бег", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Бег", result.getContent().get(0).getName());
        verify(exerciseSearchRepository).findByNameFuzzy("Бег");
        verify(exerciseMapper).toExerciseResponseListFromSearchDocument(docs);
    }

    /**
     * Проверяет fallback на prefix-поиск, если fuzzy-поиск не дал результатов.
     */
    @Test
    void searchByName_ShouldFallbackToPrefix() {
        ExerciseSearchDocument doc = new ExerciseSearchDocument(2, "Бег быстрой ходьбой", new BigDecimal("400"), 1);
        List<ExerciseSearchDocument> docs = List.of(doc);
        ExerciseResponse resp = new ExerciseResponse();
        resp.setExerciseId(2);
        resp.setName("Бег быстрой ходьбой");
        resp.setCaloriesPerHour(400);
        when(exerciseSearchRepository.findByNameFuzzy("Бе")).thenReturn(Collections.emptyList());
        when(exerciseSearchRepository.findByNamePrefix("Бе")).thenReturn(docs);
        when(exerciseMapper.toExerciseResponseListFromSearchDocument(docs)).thenReturn(List.of(resp));
        when(pageable.getOffset()).thenReturn(0L);
        when(pageable.getPageSize()).thenReturn(10);

        Page<ExerciseResponse> result = exerciseService.searchByName("Бе", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Бег быстрой ходьбой", result.getContent().get(0).getName());
        verify(exerciseSearchRepository).findByNameFuzzy("Бе");
        verify(exerciseSearchRepository).findByNamePrefix("Бе");
        verify(exerciseMapper).toExerciseResponseListFromSearchDocument(docs);
    }

    /**
     * Проверяет возврат пустого результата при отсутствии совпадений.
     */
    @Test
    void searchByName_ShouldReturnEmpty_WhenNoResults() {
        when(exerciseSearchRepository.findByNameFuzzy("Плавание")).thenReturn(Collections.emptyList());
        when(exerciseSearchRepository.findByNamePrefix("Плавание")).thenReturn(Collections.emptyList());
        when(exerciseMapper.toExerciseResponseListFromSearchDocument(Collections.emptyList())).thenReturn(Collections.emptyList());
        when(pageable.getOffset()).thenReturn(0L);
        when(pageable.getPageSize()).thenReturn(10);

        Page<ExerciseResponse> result = exerciseService.searchByName("Плавание", pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(exerciseSearchRepository).findByNameFuzzy("Плавание");
        verify(exerciseSearchRepository).findByNamePrefix("Плавание");
        verify(exerciseMapper).toExerciseResponseListFromSearchDocument(Collections.emptyList());
    }
} 