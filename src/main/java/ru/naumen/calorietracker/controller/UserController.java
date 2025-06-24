package ru.naumen.calorietracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.naumen.calorietracker.service.implementations.AccessChecker;
import ru.naumen.calorietracker.util.AuthUtils;

import java.security.Principal;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final AuthUtils authUtils;
    private final AccessChecker accessChecker;

    public UserController(AuthUtils authUtils, AccessChecker accessChecker) {
        this.authUtils = authUtils;
        this.accessChecker = accessChecker;
    }

    @GetMapping("/current")
    public ResponseEntity<Integer> getCurrentUserId(Principal principal){
        return ResponseEntity.ok(authUtils.getAuthenticatedUser(principal).getUserId());
    }

    @GetMapping("/is_admin")
    public ResponseEntity<Boolean> isUserAdmin(){
        Boolean isAdmin = accessChecker.isAdmin();
        return ResponseEntity.ok(isAdmin);
    }
}
