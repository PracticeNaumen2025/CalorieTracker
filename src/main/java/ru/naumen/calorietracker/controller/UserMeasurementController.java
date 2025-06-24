package ru.naumen.calorietracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.dto.UserMeasurementResponse;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.service.UserMeasurementService;
import ru.naumen.calorietracker.util.AuthUtils;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/measurements")
@RequiredArgsConstructor
public class UserMeasurementController {

    private final UserMeasurementService userMeasurementService;
    private final AuthUtils authUtils;


    @GetMapping("/me")
    public ResponseEntity<List<UserMeasurementResponse>> getAllMeasurementsForCurrentUser(Principal principal) {
        User user = authUtils.getAuthenticatedUser(principal);
        List<UserMeasurementResponse> measurements = userMeasurementService.getAllMeasurementsByUser(user);
        return ResponseEntity.ok(measurements);
    }
}
