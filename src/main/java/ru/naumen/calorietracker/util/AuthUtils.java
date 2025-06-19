package ru.naumen.calorietracker.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.repository.UserRepository;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;

    public void checkAuthentication(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Пользователь не аутентифицирован");
        }
    }

    public User getAuthenticatedUser(Principal principal) {
        checkAuthentication(principal);
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}
