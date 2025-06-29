package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Представление (view) агрегированных данных за день.
 * Используется для отображения суммарной информации о потребленных калориях и нутриентах.
 * <p>
 * Является неизменяемым (Immutable), так как основано на базе данных-представлении.
 */
@Entity
@Immutable
@Getter
@IdClass(DaySummaryId.class)
@Table(name = "v_day_summary")
public class DaySummary {
    /**
     * Идентификатор пользователя.
     */
    @Id
    @Column(name = "user_id")
    private Integer userId;

    /**
     * Дата, за которую собраны данные.
     */
    @Id
    @Column(name = "date")
    private LocalDate date;

    /**
     * Общая сумма потреблённых калорий за день.
     */
    @Column(name = "total_calories")
    private BigDecimal totalCalories;

    /**
     * Общее количество потребленного белка (в граммах).
     */
    @Column(name = "total_protein")
    private BigDecimal totalProtein;

    /**
     * Общее количество потребленного жира (в граммах).
     */
    @Column(name = "total_fat")
    private BigDecimal totalFat;

    /**
     * Общее количество потреблённых углеводов (в граммах).
     */
    @Column(name = "total_carbs")
    private BigDecimal totalCarbs;
}