package ru.naumen.calorietracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.calorietracker.service.UserService;

import java.security.Principal;

@Component
public class BaseController {
    @Autowired
    protected UserService userService;

    protected Integer getUserIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userService.getUserByUsername(username).userId();
    }
}
