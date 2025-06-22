package ru.naumen.calorietracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.dto.UserResponse;
import ru.naumen.calorietracker.dto.UserUpdateRequest;
import ru.naumen.calorietracker.service.UserService;
import ru.naumen.calorietracker.util.AuthUtils;

import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    private final UserService userService;
    private final AuthUtils authUtils;

    public ProfileController(UserService userService, AuthUtils authUtils) {
        this.userService = userService;
        this.authUtils = authUtils;
    }

    @GetMapping
    public ResponseEntity<UserResponse> getProfile(Principal principal) {
        authUtils.checkAuthentication(principal);
        UserResponse userResponse = userService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping
    public ResponseEntity<UserResponse> updateProfile(
            Principal principal,
            @RequestBody UserUpdateRequest updateRequest) {

        authUtils.checkAuthentication(principal);
        UserResponse updatedUser = userService.updateUserProfile(principal.getName(), updateRequest);
        return ResponseEntity.ok(updatedUser);
    }
}
