package ru.naumen.calorietracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "daysummary")
@Getter
@Setter
public class DaySummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "summary_id")
    private Integer summaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "total_calories", nullable = false)
    private BigDecimal totalCalories;

    @Column(name = "total_protein", nullable = false)
    private BigDecimal totalProtein;

    @Column(name = "total_fat", nullable = false)
    private BigDecimal totalFat;

    @Column(name = "total_carbs", nullable = false)
    private BigDecimal totalCarbs;
}