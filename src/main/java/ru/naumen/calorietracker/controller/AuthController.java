package ru.naumen.calorietracker.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.dto.UserRegisterRequest;
import ru.naumen.calorietracker.dto.UserResponse;
import ru.naumen.calorietracker.service.UserService;

/**
 * REST контроллер для аутентификации пользователей.
 * Предоставляет endpoints для регистрации и других операций, связанных с авторизацией.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint для регистрации нового пользователя.
     *
     * @param request DTO с данными для регистрации пользователя
     * @return ResponseEntity с информацией о зарегистрированном пользователе в случае успеха,
     *         или 400 Bad Request при ошибке регистрации
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        try {
            UserResponse response = userService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}