package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Цели пользователя по снижению или набору веса и распределению нутриентов.
 */
@Entity
@Table(name = "usergoals")
@Getter
@Setter
public class UserGoal {
    /**
     * Уникальный идентификатор цели.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Integer goalId;

    /**
     * Пользователь, к которому относится цель.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Дата начала действия цели.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * Дата окончания действия цели.
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Целевой вес в килограммах.
     */
    @Column(name = "target_weight_kg")
    private BigDecimal targetWeightKg;

    /**
     * Цель по ежедневному потреблению калорий.
     */
    @Column(name = "daily_calorie_goal")
    private Integer dailyCalorieGoal;

    /**
     * Целевой процент белка от общего потребления калорий.
     */
    @Column(name = "protein_percentage")
    private BigDecimal proteinPercentage;

    /**
     * Целевой процент жиров от общего потребления калорий.
     */
    @Column(name = "fat_percentage")
    private BigDecimal fatPercentage;

    /**
     * Целевой процент углеводов от общего потребления калорий.
     */
    @Column(name = "carb_percentage")
    private BigDecimal carbPercentage;
}