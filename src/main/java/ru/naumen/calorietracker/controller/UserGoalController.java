package ru.naumen.calorietracker.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.dto.PageResponse;
import ru.naumen.calorietracker.dto.UserGoalCreateRequest;
import ru.naumen.calorietracker.dto.UserGoalResponse;
import ru.naumen.calorietracker.dto.UserGoalUpdateRequest;
import ru.naumen.calorietracker.service.UserGoalService;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/goals")
public class UserGoalController extends BaseController {

    private final UserGoalService userGoalService;

    @Autowired
    public UserGoalController(UserGoalService userGoalService) {
        this.userGoalService = userGoalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserGoalResponse createGoal(
            @RequestBody @Valid UserGoalCreateRequest request,
            Principal principal) {
        Integer currentUserId = getUserIdFromPrincipal(principal);
        return userGoalService.createUserGoal(request, currentUserId);
    }

    @PutMapping
    public UserGoalResponse updateGoal(
            @RequestBody @Valid UserGoalUpdateRequest request,
            Principal principal) {
        Integer currentUserId = getUserIdFromPrincipal(principal);
        return userGoalService.updateUserGoal(request, currentUserId);
    }

    @GetMapping("/{ownerUserId}")
    public PageResponse<UserGoalResponse> getGoals(
            @PathVariable Integer ownerUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal
    ) {
        Integer currentUserId = getUserIdFromPrincipal(principal);
        Pageable pageable = PageRequest.of(page, size);
        return userGoalService.getUserGoalsByUserId(ownerUserId, currentUserId, pageable);
    }

    @GetMapping("/active")
    public UserGoalResponse getGoalByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Principal principal
    ) {
        Integer currentUserId = getUserIdFromPrincipal(principal);
        return userGoalService.getUserGoalByUserIdAndDate(currentUserId, currentUserId, date);
    }

    @DeleteMapping("/{goalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGoals(
            @PathVariable Integer goalId,
            Principal principal) {
        Integer userid = getUserIdFromPrincipal(principal);
        userGoalService.deleteUserGoalByUserId(userid, goalId);
    }

}
