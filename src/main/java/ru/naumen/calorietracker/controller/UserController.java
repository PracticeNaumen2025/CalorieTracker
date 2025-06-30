package ru.naumen.calorietracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.naumen.calorietracker.service.implementations.AccessChecker;
import ru.naumen.calorietracker.util.AuthUtils;

import java.security.Principal;

/**
 * Контроллер для получения информации о текущем пользователе и проверке прав доступа.
 * <p>
 * Позволяет получить идентификатор текущего аутентифицированного пользователя
 * и проверить, имеет ли пользователь роль администратора.
 */
@RestController
@RequestMapping("api/user")
public class UserController {
    private final AuthUtils authUtils;
    private final AccessChecker accessChecker;

    public UserController(AuthUtils authUtils, AccessChecker accessChecker) {
        this.authUtils = authUtils;
        this.accessChecker = accessChecker;
    }

    /**
     * Получить идентификатор текущего аутентифицированного пользователя.
     *
     * @param principal объект с данными аутентификации пользователя
     * @return ResponseEntity с идентификатором пользователя
     */
    @GetMapping("/current")
    public ResponseEntity<Integer> getCurrentUserId(Principal principal){
        return ResponseEntity.ok(authUtils.getAuthenticatedUser(principal).getUserId());
    }

    /**
     * Проверить, является ли текущий пользователь администратором.
     *
     * @return ResponseEntity с логическим значением:
     * true - если пользователь имеет роль администратора,
     * false - в противном случае
     */
    @GetMapping("/is_admin")
    public ResponseEntity<Boolean> isUserAdmin(){
        Boolean isAdmin = accessChecker.isAdmin();
        return ResponseEntity.ok(isAdmin);
    }
}
