package ru.naumen.calorietracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.naumen.calorietracker.dto.FoodEntryDTO;
import ru.naumen.calorietracker.model.FoodEntry;

@Mapper(componentModel = "spring")
public interface FoodEntryMapper {
    @Mapping(target = "mealId", source = "meal.mealId")
    @Mapping(target = "productId", source = "product.productId")
    FoodEntryDTO toFoodEntryDTO(FoodEntry foodEntry);
}
