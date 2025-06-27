package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.model.Role;

/**
 * Сервис для работы с ролями пользователей.
 */
public interface RoleService {
    /**
     * Возвращает роль пользователя по умолчанию.
     * @return Объект Role, представляющий роль пользователя по умолчанию.
     */
    public Role getDefaultUserRole();
}
