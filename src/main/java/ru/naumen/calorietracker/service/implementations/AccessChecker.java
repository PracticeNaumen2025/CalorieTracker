package ru.naumen.calorietracker.service.implementations;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class AccessChecker {
    public boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isOwner(Integer entityUserId, Integer currentUserId) {
        return entityUserId.equals(currentUserId);
    }

    public void checkAccess(Integer entityUserId, Integer currentUserId) {
        if (!isOwner(entityUserId, currentUserId) && !isAdmin()) {
            throw new AccessDeniedException("Нет доступа");
        }
    }
}