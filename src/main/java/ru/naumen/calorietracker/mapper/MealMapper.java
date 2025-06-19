package ru.naumen.calorietracker.mapper;

import org.mapstruct.Mapper;
import ru.naumen.calorietracker.dto.MealDTO;
import ru.naumen.calorietracker.model.Meal;

@Mapper(componentModel = "spring")
public interface MealMapper {
    MealDTO toMealDTO(Meal meal);
    Meal toMeal(MealDTO mealDTO);
}
