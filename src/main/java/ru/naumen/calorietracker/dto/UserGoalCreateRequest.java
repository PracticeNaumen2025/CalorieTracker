package ru.naumen.calorietracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public record UserGoalCreateRequest(
        @PositiveOrZero(message = "id пользователя не должен быть отрицательным")
        int userId,

        LocalDate startDate,
        LocalDate endDate,

        @Positive(message = "Желаемый вес должен быть положительным")
        double targetWeightKg,

        @Positive
        int dailyCalorieGoal,

        @PositiveOrZero(message = "Процент белков должен быть положительным!")
        double proteinPercentage,

        @PositiveOrZero(message = "Процент жиров должен быть положительным!")
        double fatPercentage,

        @PositiveOrZero(message = "Процент углеводов должен быть положительным!")
        double carbPercentage
) {
    @AssertTrue(message = "Дата начала должна быть меньше или равна дате окончания")
    @JsonIgnore
    public boolean isStartDateBeforeOrEqualEndDate() {
        return startDate != null && endDate != null && !startDate.isAfter(endDate);
    }

    @AssertTrue(message = "Сумма процентов белков, жиров и углеводов должна быть равна 100%")
    @JsonIgnore
    public boolean isPercentageSumEqualHundred() {
        return proteinPercentage + fatPercentage + carbPercentage == 100;
    }
}
