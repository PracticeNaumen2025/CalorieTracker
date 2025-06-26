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

    @GetMapping("/reports")
    public String reportsView() {
        return "reports";
    }

    @GetMapping("/diary")
    public String diaryView() {
        return "diary";
    }

    @GetMapping("/products")
    public String productsView(){
        return "products";
    }

    @GetMapping("/admin")
    public String adminView(){
        return "admin";
    }

    @GetMapping("/admin/users")
    public String adminUsersView(){
        return "admin-users";
    }

    @GetMapping("/admin/products")
    public String adminProductsView(){
        return "admin-products";
    }

    @GetMapping("/admin/exercises")
    public String adminExercisesView(){
        return "admin-exercises";
    }

    @GetMapping("/exercises")
    public String exercisesView() {
        return "exercises";
    }

    @GetMapping("/manage_exercises")
    public String manage_exercisesView(){
        return "manage_exercises";
    }
}
