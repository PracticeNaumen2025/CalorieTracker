package ru.naumen.calorietracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/profile")
    public String profileView() {
        return "profile";
    }

    @GetMapping("/diary")
    public String diaryView() {
        return "diary";
    }
}
