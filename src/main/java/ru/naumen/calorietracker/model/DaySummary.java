package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Immutable
@Getter
@IdClass(DaySummaryId.class)
@Table(name = "v_day_summary")
public class DaySummary {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "total_calories")
    private BigDecimal totalCalories;

    @Column(name = "total_protein")
    private BigDecimal totalProtein;

    @Column(name = "total_fat")
    private BigDecimal totalFat;

    @Column(name = "total_carbs")
    private BigDecimal totalCarbs;
}