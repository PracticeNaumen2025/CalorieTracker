package ru.naumen.calorietracker.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class DaySummaryId implements Serializable {
    private Integer userId;
    private LocalDate date;
} 