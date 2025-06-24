package ru.naumen.calorietracker.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.naumen.calorietracker.dto.UserResponse;
import ru.naumen.calorietracker.dto.UserUpdateRequest;
import ru.naumen.calorietracker.service.UserService;
import ru.naumen.calorietracker.util.AuthUtils;

import java.io.IOException;
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

    @PostMapping(value = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> uploadProfilePhoto(
            Principal principal,
            @RequestParam("file") MultipartFile file) {

        authUtils.checkAuthentication(principal);

        try {
            UserResponse updatedUser = userService.updateUserPhoto(principal.getName(), file);
            return ResponseEntity.ok(updatedUser);
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
