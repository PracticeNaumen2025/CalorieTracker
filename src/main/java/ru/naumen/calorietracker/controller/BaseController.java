package ru.naumen.calorietracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.repository.UserRepository;
import ru.naumen.calorietracker.service.UserService;

import java.security.Principal;

@Component
public class BaseController {
    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepository userRepository;

    protected Integer getUserIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userService.getUserByUsername(username).userId();
    }

    protected void checkAuthentication(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Пользователь не аутентифицирован");
        }
    }

    protected User getAuthenticatedUser(Principal principal) {
        checkAuthentication(principal);
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}
