package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.naumen.calorietracker.dto.FoodEntryDTO;
import ru.naumen.calorietracker.dto.ProductResponse;
import ru.naumen.calorietracker.mapper.FoodEntryMapper;
import ru.naumen.calorietracker.mapper.ProductMapper;
import ru.naumen.calorietracker.model.FoodEntry;
import ru.naumen.calorietracker.model.Meal;
import ru.naumen.calorietracker.model.Product;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.repository.FoodEntryRepository;
import ru.naumen.calorietracker.repository.MealRepository;
import ru.naumen.calorietracker.service.FoodEntryService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodEntryServiceImpl implements FoodEntryService {
    private final FoodEntryRepository foodEntryRepository;
    private final MealRepository mealRepository;
    private final FoodEntryMapper foodEntryMapper;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public FoodEntryDTO addFoodEntry(Meal meal, ProductResponse productResponse, BigDecimal portionGrams) {
        FoodEntry entry = new FoodEntry();
        entry.setMeal(meal);
        Product product = productMapper.toProduct(productResponse);
        entry.setProduct(product);
        entry.setPortionGrams(portionGrams);

        BigDecimal factor = portionGrams.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        entry.setCalories(product.getCaloriesPer100g().multiply(factor));
        entry.setProteinG(product.getProteinPer100g().multiply(factor));
        entry.setFatG(product.getFatPer100g().multiply(factor));
        entry.setCarbsG(product.getCarbsPer100g().multiply(factor));

        FoodEntry savedEntry = foodEntryRepository.save(entry);

        return foodEntryMapper.toFoodEntryDTO(savedEntry);
    }

    @Override
    public List<FoodEntryDTO> getFoodEntriesByMealId(Integer mealId, User user) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found with id: " + mealId));

        if (!meal.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Access denied: meal does not belong to user");
        }

        List<FoodEntry> entries = foodEntryRepository.findByMealMealId(mealId);
        return entries.stream()
                .map(foodEntryMapper::toFoodEntryDTO)
                .toList();
    }

    @Override
    public List<FoodEntry> getFoodEntriesByMeals(List<Meal> meals) {
        return foodEntryRepository.findByMealIn(meals);
    }

    @Override
    @Transactional
    public FoodEntryDTO updatePortionGrams(Integer entryId, BigDecimal newPortionGrams, User user) {
        FoodEntry entry = foodEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Food entry not found with id: " + entryId));

        if (!entry.getMeal().getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Access denied: entry does not belong to user");
        }

        entry.setPortionGrams(newPortionGrams);

        BigDecimal factor = newPortionGrams.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        Product product = entry.getProduct();
        entry.setCalories(product.getCaloriesPer100g().multiply(factor));
        entry.setProteinG(product.getProteinPer100g().multiply(factor));
        entry.setFatG(product.getFatPer100g().multiply(factor));
        entry.setCarbsG(product.getCarbsPer100g().multiply(factor));

        return foodEntryMapper.toFoodEntryDTO(foodEntryRepository.save(entry));
    }

    @Override
    @Transactional
    public void deleteFoodEntry(Integer entryId, User user) {
        FoodEntry entry = foodEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Food entry not found with id: " + entryId));

        if (!entry.getMeal().getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Access denied: entry does not belong to user");
        }

        foodEntryRepository.delete(entry);
    }

    @Override
    public FoodEntry getFoodEntryById(Integer entryId) {
        return foodEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("FoodEntry not found with id: " + entryId));
    }
}
