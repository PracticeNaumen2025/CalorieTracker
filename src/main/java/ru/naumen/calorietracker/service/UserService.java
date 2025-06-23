package ru.naumen.calorietracker.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import ru.naumen.calorietracker.dto.UserRegisterRequest;
import ru.naumen.calorietracker.dto.UserResponse;
import ru.naumen.calorietracker.dto.UserUpdateRequest;

import java.io.IOException;

public interface UserService extends UserDetailsService {
    /**
     * Регистрирует пользователя в системе
     */
    UserResponse register(UserRegisterRequest request);

    UserResponse getUserByUsername(String username);

    UserResponse updateUserProfile(String username, UserUpdateRequest updateRequest);

    UserResponse updateUserPhoto(String username, MultipartFile file) throws IOException;
}
