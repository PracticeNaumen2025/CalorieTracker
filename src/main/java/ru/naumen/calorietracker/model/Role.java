package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность, представляющая роль пользователя в системе (например, ADMIN, USER).
 * Используется для авторизации.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {
    /**
     * Уникальный идентификатор роли.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * Уникальное имя роли.
     */
    @Column(name = "role_name", nullable = false, unique = true, length = 20)
    private String roleName;
}
