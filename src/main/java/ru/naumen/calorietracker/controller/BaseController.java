package ru.naumen.calorietracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.repository.UserRepository;
import ru.naumen.calorietracker.service.UserService;

import java.security.Principal;

/**
 * Базовый контроллер, предоставляющий вспомогательные методы для работы с аутентификацией и пользователем.
 */
@Component
public class BaseController {
    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepository userRepository;

    /**
     * Получает ID пользователя по объекту Principal.
     *
     * @param principal объект Principal с данными аутентифицированного пользователя
     * @return ID пользователя
     */
    protected Integer getUserIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userService.getUserByUsername(username).userId();
    }

    /**
     * Проверяет, что пользователь аутентифицирован.
     *
     * @param principal объект Principal с данными аутентифицированного пользователя
     * @throws RuntimeException если пользователь не аутентифицирован (principal == null)
     */
    protected void checkAuthentication(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Пользователь не аутентифицирован");
        }
    }

    /**
     * Получает объект User аутентифицированного пользователя.
     *
     * @param principal объект Principal с данными аутентифицированного пользователя
     * @return объект User из базы данных
     * @throws RuntimeException если пользователь не найден или не аутентифицирован
     */
    protected User getAuthenticatedUser(Principal principal) {
        checkAuthentication(principal);
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}
