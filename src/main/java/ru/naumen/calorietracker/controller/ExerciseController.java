package ru.naumen.calorietracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.dto.ExerciseCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseResponse;
import ru.naumen.calorietracker.dto.ExerciseUpdateRequest;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.service.ExerciseService;
import ru.naumen.calorietracker.util.AuthUtils;

import java.security.Principal;


@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final AuthUtils authUtils;

    @GetMapping
    public Page<ExerciseResponse> getAllExercises(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return exerciseService.getAllExercises(pageable);
    }

    @GetMapping("/{id}")
    public ExerciseResponse getExerciseById(@PathVariable Integer id) {
        return exerciseService.getExerciseById(id);
    }

    @PostMapping
    public ExerciseResponse createExercise(@Valid @RequestBody ExerciseCreateRequest request, Principal principal) {
        User currentUser = authUtils.getAuthenticatedUser(principal);
        return exerciseService.createExercise(request, currentUser);
    }

    @PutMapping
    public ExerciseResponse updateExercise(@Valid @RequestBody ExerciseUpdateRequest request, Principal principal) {
        User currentUser = authUtils.getAuthenticatedUser(principal);
        return exerciseService.updateExercise(request, currentUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteExercise(@PathVariable Integer id) {
        exerciseService.deleteExercise(id);
    }

    @GetMapping("/search")
    public Page<ExerciseResponse> search(@RequestParam String name,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return exerciseService.searchByName(name, pageable);
    }
} 