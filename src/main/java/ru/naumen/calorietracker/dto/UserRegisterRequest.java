package ru.naumen.calorietracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @Size(min = 3, max = 20, message = "Username от 3 до 20 символов")
        String username,

        @Size(min = 6, message = "Пароль минимум 6 символов")
        String password,

        @Email(message = "Email должен быть корректным")
        String email
) {}
