package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.naumen.calorietracker.dto.ExerciseCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseResponse;
import ru.naumen.calorietracker.dto.ExerciseUpdateRequest;
import ru.naumen.calorietracker.mapper.ExerciseMapper;
import ru.naumen.calorietracker.model.Exercise;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.model.elastic.ExerciseSearchDocument;
import ru.naumen.calorietracker.repository.ExerciseRepository;
import ru.naumen.calorietracker.repository.search.ExerciseSearchRepository;
import ru.naumen.calorietracker.service.ExerciseService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseSearchRepository exerciseSearchRepository;
    private final ExerciseMapper exerciseMapper;
    private final AccessChecker accessChecker;

    @Override
    public Page<ExerciseResponse> getAllExercises(Pageable pageable) {
        Page<Exercise> page = exerciseRepository.findAll(pageable);
        List<ExerciseResponse> responses = exerciseMapper.toExerciseResponseList(page.getContent());
        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    @Override
    public ExerciseResponse getExerciseById(Integer id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Упражнение с ID " + id + " не найдено"));
        return exerciseMapper.toExerciseResponse(exercise);
    }

    @Override
    @Transactional
    public ExerciseResponse createExercise(ExerciseCreateRequest request, User currentUser) {
        Exercise exercise = exerciseMapper.toExercise(request);
        exercise.setCreator(currentUser);
        Exercise saved = exerciseRepository.save(exercise);
        return exerciseMapper.toExerciseResponse(saved);
    }

    @Override
    @Transactional
    public ExerciseResponse updateExercise(ExerciseUpdateRequest request, User currentUser) {
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Упражнение с ID " + request.getExerciseId() + " не найдено"));

        accessChecker.checkAccess(exercise.getCreator().getUserId(), currentUser.getUserId());

        Exercise updated = exerciseMapper.toExercise(request);
        exercise.setName(updated.getName());
        exercise.setCaloriesPerHour(updated.getCaloriesPerHour());
        Exercise saved = exerciseRepository.save(exercise);
        return exerciseMapper.toExerciseResponse(saved);
    }

    @Override
    @Transactional
    public void deleteExercise(Integer id) {
        exerciseRepository.deleteById(id);
    }

    @Override
    public Page<ExerciseResponse> searchByName(String name, Pageable pageable) {
        List<ExerciseSearchDocument> docs = exerciseSearchRepository.findByNameFuzzy(name);
        if (docs.isEmpty())
            docs = exerciseSearchRepository.findByNamePrefix(name);
        List<ExerciseResponse> responses = exerciseMapper.toExerciseResponseListFromSearchDocument(docs);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responses.size());
        List<ExerciseResponse> pageContent = responses.subList(start, end);
        return new PageImpl<>(pageContent, pageable, responses.size());
    }
} 