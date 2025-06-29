package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Сущность, представляющая приём пищи пользователя.
 * Хранит дату, время и тип приёма пищи (например, завтрак, обед).
 */
@Entity
@Table(name = "meals")
@Getter
@Setter
public class Meal {
    /**
     * Уникальный идентификатор приёма пищи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Integer mealId;

    /**
     * Пользователь, которому принадлежит приём пищи.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Дата и время приёма пищи.
     */
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    /**
     * Тип приёма пищи (например, "Завтрак", "Ужин").
     */
    @Column(name = "meal_type", length = 20)
    private String mealType;
}