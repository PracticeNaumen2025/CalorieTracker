package ru.naumen.calorietracker.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Составной идентификатор для сущности {@link ru.naumen.calorietracker.model.DaySummary}.
 * Используется для связывания пользователя и даты в представлении агрегации данных.
 */
@Getter
@Setter
@EqualsAndHashCode
public class DaySummaryId implements Serializable {
    /**
     * Идентификатор пользователя.
     */
    private Integer userId;

    /**
     * Дата, за которую собраны данные.
     */
    private LocalDate date;
} 