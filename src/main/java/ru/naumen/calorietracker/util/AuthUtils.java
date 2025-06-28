package ru.naumen.calorietracker.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.repository.UserRepository;

import java.security.Principal;

/**
 * Утилитарный компонент для работы с текущим аутентифицированным пользователем.
 * Обеспечивает безопасный доступ к данным пользователя через {@link Principal}.
 */
@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;

    /**
     * Проверяет, что пользователь аутентифицирован.
     *
     * @param principal объект {@link Principal}, полученный из контекста безопасности
     * @throws RuntimeException если пользователь не аутентифицирован
     */
    public void checkAuthentication(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Пользователь не аутентифицирован");
        }
    }

    /**
     * Возвращает сущность {@link User} текущего аутентифицированного пользователя.
     *
     * @param principal объект {@link Principal}, полученный из контекста безопасности
     * @return объект {@link User}, соответствующий имени пользователя
     * @throws RuntimeException если пользователь не найден в базе данных
     */
    public User getAuthenticatedUser(Principal principal) {
        checkAuthentication(principal);
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}
