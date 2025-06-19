package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.naumen.calorietracker.dto.MealDTO;
import ru.naumen.calorietracker.mapper.MealMapper;
import ru.naumen.calorietracker.model.Meal;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.repository.MealRepository;
import ru.naumen.calorietracker.service.MealService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    @Override
    public MealDTO createMeal(User user, LocalDateTime dateTime, String mealType) {
        Meal meal = new Meal();
        meal.setUser(user);
        meal.setDateTime(dateTime);
        meal.setMealType(mealType);
        Meal savedMeal = mealRepository.save(meal);
        return mealMapper.toMealDTO(savedMeal);
    }

    @Override
    public Meal getMealById(Integer mealId) {
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found with id: " + mealId));
    }

    @Override
    public List<Meal> getMealsByUserAndDate(User user, LocalDate date) {
        return mealRepository.findAllByUserAndDateTimeBetween(
                user, date.atStartOfDay(), date.atTime(23, 59, 59)
        );
    }
}
