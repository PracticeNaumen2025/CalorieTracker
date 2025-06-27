package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.MealDTO;
import ru.naumen.calorietracker.model.Meal;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с приемами пищи.
 */
public interface MealService {
    /**
     * Создает новый прием пищи для пользователя.
     * @param user Пользователь, для которого создается прием пищи.
     * @param dateTime Дата и время приема пищи.
     * @param mealType Тип приема пищи (например, "Завтрак", "Обед").
     * @return Объект MealDTO, представляющий созданный прием пищи.
     */
    MealDTO createMeal(User user, LocalDateTime dateTime, String mealType);
    /**
     * Возвращает прием пищи по его идентификатору.
     * @param mealId Идентификатор приема пищи.
     * @return Объект Meal, если найден, иначе выбрасывает исключение.
     */
    Meal getMealById(Integer mealId);
    /**
     * Возвращает список приемов пищи для указанного пользователя и даты.
     * @param user Пользователь, для которого запрашиваются приемы пищи.
     * @param date Дата, за которую запрашиваются приемы пищи.
     * @return Список объектов Meal.
     */
    List<Meal> getMealsByUserAndDate(User user, LocalDate date);
    /**
     * Удаляет прием пищи по его идентификатору.
     * @param mealId Идентификатор приема пищи для удаления.
     */
    void deleteMeal(Integer mealId);
}
