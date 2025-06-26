package ru.naumen.calorietracker.controller;

import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import ru.naumen.calorietracker.dto.ExerciseEntryCreateRequest;
import ru.naumen.calorietracker.dto.ExerciseEntryResponse;
import ru.naumen.calorietracker.dto.ExerciseEntryUpdateRequest;
import ru.naumen.calorietracker.service.ExerciseEntryService;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/exercise/entry")
public class ExerciseEntryController {

    private final ExerciseEntryService exerciseEntryService;
    private final AuthUtils authUtils;

    @GetMapping("/{id}")
    public ExerciseEntryResponse getExerciseEntry(@PathVariable Integer id, Principal principal) {
        User currentUser = authUtils.getAuthenticatedUser(principal);
        return exerciseEntryService.getExerciseEntryById(id, currentUser);
    }

    @GetMapping
    public java.util.List<ExerciseEntryResponse> getEntriesByDate(@RequestParam("start") String start,
                                                                 @RequestParam("end") String end,
                                                                 Principal principal) {
        User currentUser = authUtils.getAuthenticatedUser(principal);
        java.time.LocalDateTime startDate = java.time.LocalDateTime.parse(start);
        java.time.LocalDateTime endDate = java.time.LocalDateTime.parse(end);
        return exerciseEntryService.getExerciseEntriesByDate(startDate, endDate, currentUser);
    }

    @PostMapping
    public ExerciseEntryResponse createEntry(@RequestBody ExerciseEntryCreateRequest request, Principal principal) {
        User currentUser = authUtils.getAuthenticatedUser(principal);
        return exerciseEntryService.createExerciseEntry(request, currentUser);
    }

    @PutMapping
    public ExerciseEntryResponse updateEntry(@RequestBody ExerciseEntryUpdateRequest request, Principal principal) {
        User currentUser = authUtils.getAuthenticatedUser(principal);
        return exerciseEntryService.updateExerciseEntry(request, currentUser);
    }
}
