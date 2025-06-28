package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Исторические измерения пользователя: вес и рост на определённую дату.
 */
@Entity
@Table(name = "usermeasurements")
@Getter
@Setter
public class UserMeasurement {
    /**
     * Уникальный идентификатор измерения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "measurement_id")
    private Integer measurementId;

    /**
     * Пользователь, к которому относится измерение.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Дата, на которую было сделано измерение.
     */
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /**
     * Вес в килограммах.
     */
    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    /**
     * Рост в сантиметрах.
     */
    @Column(name = "height_cm")
    private BigDecimal heightCm;
}