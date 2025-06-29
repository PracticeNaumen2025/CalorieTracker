package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Сущность, представляющая упражнение.
 * Хранит информацию о названии, калориях, авторе и времени последнего обновления.
 */
@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
public class Exercise {
    /**
     * Уникальный идентификатор упражнения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Integer exerciseId;

    /**
     * Название упражнения.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Количество сжигаемых калорий в час.
     */
    @Column(name = "calories_per_hour")
    private BigDecimal caloriesPerHour;

    /**
     * Время последнего обновления записи.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * Пользователь, создавший упражнение.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User creator;
}
