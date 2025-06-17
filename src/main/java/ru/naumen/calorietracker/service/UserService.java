package ru.naumen.calorietracker.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.naumen.calorietracker.dto.UserRegisterRequest;
import ru.naumen.calorietracker.dto.UserResponse;

public interface UserService extends UserDetailsService {
    /**
     * Регистрирует пользователя в системе
     */
    UserResponse register(UserRegisterRequest request);

    UserResponse getUserByUsername(String username);
}
