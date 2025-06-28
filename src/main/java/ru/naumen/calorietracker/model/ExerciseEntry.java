package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сущность, представляющая запись о выполнении упражнения пользователем.
 * Хранит информацию о пользователе, упражнении, дате, длительности и потраченных калориях.
 */
@Entity
@Table(name = "exerciseentries")
@Getter
@Setter
public class ExerciseEntry {
    /**
     * Уникальный идентификатор записи об упражнении.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Integer entryId;

    /**
     * Пользователь, выполнивший упражнение.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Упражнение, которое выполнялось.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    /**
     * Дата и время выполнения упражнения.
     */
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    /**
     * Длительность упражнения в минутах.
     */
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    /**
     * Рассчитанные калории, потраченные за выполнение.
     */
    @Column(name = "calories_burned")
    private BigDecimal caloriesBurned;
}