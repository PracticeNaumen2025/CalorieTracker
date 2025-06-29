package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сущность пользователя приложения.
 * Реализует интерфейс {@link org.springframework.security.core.userdetails.UserDetails}
 * для интеграции с Spring Security.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {
    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    /**
     * Уникальное имя пользователя (логин).
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Хеш пароля пользователя.
     */
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    /**
     * URL фотографии профиля пользователя.
     */
    @Column(name = "photo_url", length = 255)
    private String photoUrl;

    /**
     * Роли пользователя.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Дата рождения пользователя.
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /**
     * Пол пользователя (например, "мужской", "женский").
     */
    @Column(name = "gender", length = 10)
    private String gender;

    /**
     * Рост пользователя в сантиметрах.
     */
    @Column(name = "height_cm")
    private BigDecimal heightCm;

    /**
     * Вес пользователя в килограммах.
     */
    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    /**
     * Уровень физической активности.
     */
    @Column(name = "activity_level", length = 20)
    private String activityLevel;

    /**
     * Флаг мягкого удаления.
     */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    /**
     * Дата создания пользователя.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Дата последнего обновления.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Устанавливает дату создания при первом сохранении.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Обновляет дату изменения перед сохранением.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Возвращает коллекцию полномочий пользователя для Spring Security.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRoleName()))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает хеш пароля.
     */
    @Override
    public String getPassword() {
        return passwordHash;
    }

    /**
     * Возвращает имя пользователя (логин).
     */
    @Override
    public String getUsername() {
        return username;
    }
}
