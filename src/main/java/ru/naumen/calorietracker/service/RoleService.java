package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.model.Role;

public interface RoleService {
    /**
     * Получить роль по умолчанию для пользователей
     */
    public Role getDefaultUserRole();
}
