package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.naumen.calorietracker.dto.*;
import ru.naumen.calorietracker.model.FoodEntry;
import ru.naumen.calorietracker.model.Meal;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {
    private final MealService mealService;
    private final FoodEntryService foodEntryService;
    private final ProductService productService;
    private final DaySummaryService daySummaryService;

    @Override
    public MealDTO createMeal(CreateMealRequest request, User user) {
        return mealService.createMeal(user, request.dateTime(), request.mealType());
    }

    @Override
    public FoodEntryDTO addFoodEntry(AddFoodEntryRequest request) {
        Meal meal = mealService.getMealById(request.mealId());
        ProductResponse product = productService.getProductById(request.productId());
        return foodEntryService.addFoodEntry(meal, product, request.portionGrams());
    }

    @Override
    public DaySummaryDTO getDaySummary(String date, User user) {
        LocalDate localDate = LocalDate.parse(date); // Формат: "2025-06-19"
        return daySummaryService.getSummaryByUserAndDate(user, localDate);
    }

    @Override
    public List<FoodEntryDTO> getFoodEntriesByMeal(Integer mealId, User user) {
        return foodEntryService.getFoodEntriesByMealId(mealId, user);
    }

    @Override
    public List<MealWithEntriesResponse> getMealsWithFoodEntriesByDate(User user, LocalDate date) {
        List<Meal> meals = mealService.getMealsByUserAndDate(user, date);
        List<FoodEntry> allEntries = foodEntryService.getFoodEntriesByMeals(meals);

        return meals.stream().map(meal -> {
            List<FoodEntryWithProductResponse> entries = allEntries.stream()
                    .filter(entry -> entry.getMeal().getMealId().equals(meal.getMealId()))
                    .map(entry -> new FoodEntryWithProductResponse(
                            entry.getEntryId(),
                            entry.getProduct().getName(),
                            entry.getPortionGrams(),
                            entry.getCalories(),
                            entry.getProteinG(),
                            entry.getFatG(),
                            entry.getCarbsG()
                    )).toList();

            return new MealWithEntriesResponse(
                    meal.getMealId(),
                    meal.getDateTime(),
                    meal.getMealType(),
                    entries
            );
        }).toList();
    }

    @Override
    public List<MealDTO> getMealsByDate(User user, LocalDate date) {
        List<Meal> meals = mealService.getMealsByUserAndDate(user, date);
        return meals.stream()
                .map(meal -> new MealDTO(meal.getMealId(), meal.getDateTime(), meal.getMealType()))
                .toList();
    }

    @Override
    public FoodEntryDTO updateFoodEntryPortion(Integer entryId, BigDecimal newPortionGrams, User user) {
        return foodEntryService.updatePortionGrams(entryId, newPortionGrams, user);
    }

    @Override
    public void deleteFoodEntry(Integer entryId, User user) {
        foodEntryService.deleteFoodEntry(entryId, user);
    }

    @Override
    @Transactional
    public void deleteMeal(Integer mealId, User user) {
        Meal meal = mealService.getMealById(mealId);

        if (!meal.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("У вас нет прав удалять этот приём пищи");
        }

        List<FoodEntry> foodEntries = foodEntryService.getFoodEntriesByMealId(mealId, user).stream()
                .map(dto -> foodEntryService.getFoodEntryById(dto.getEntryId()))
                .toList();

        for (FoodEntry entry : foodEntries) {
            foodEntryService.deleteFoodEntry(entry.getEntryId(), user);
        }

        mealService.deleteMeal(mealId);
    }
}
