package ru.naumen.calorietracker.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.dto.UserGoalCreateRequest;
import ru.naumen.calorietracker.dto.UserGoalResponse;
import ru.naumen.calorietracker.dto.UserGoalUpdateRequest;
import ru.naumen.calorietracker.service.UserGoalService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

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
    public List<UserGoalResponse> getGoals(
            @PathVariable Integer ownerUserId,
            Principal principal
    ) {
        Integer currentUserId = getUserIdFromPrincipal(principal);
        return userGoalService.getUserGoalsByUserId(ownerUserId, currentUserId);
    }

    @GetMapping("/{ownerUserId}/active")
    public UserGoalResponse getGoalByDate(
            @PathVariable Integer ownerUserId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Principal principal
    ) {
        Integer currentUserId = getUserIdFromPrincipal(principal);
        return userGoalService.getUserGoalByUserIdAndDate(ownerUserId, currentUserId, date);
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
