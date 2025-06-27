package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.*;
import ru.naumen.calorietracker.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для управления дневником питания пользователя.
 */
public interface DiaryService {
    /**
     * Создает новый прием пищи для пользователя.
     * @param request Запрос на создание приема пищи.
     * @param user Пользователь, для которого создается прием пищи.
     * @return Объект MealDTO, представляющий созданный прием пищи.
     */
    MealDTO createMeal(CreateMealRequest request, User user);
    /**
     * Добавляет запись о еде в существующий прием пищи.
     * @param request Запрос на добавление записи о еде.
     * @return Объект FoodEntryDTO, представляющий добавленную запись о еде.
     */
    FoodEntryDTO addFoodEntry(AddFoodEntryRequest request);
    /**
     * Возвращает сводку по питанию за указанный день для пользователя.
     * @param date Дата в формате строки (например, "2025-06-19").
     * @param user Пользователь, для которого запрашивается сводка.
     * @return Объект DaySummaryDTO, содержащий сводку за день.
     */
    DaySummaryDTO getDaySummary(String date, User user);
    /**
     * Возвращает список записей о еде для указанного приема пищи.
     * @param mealId Идентификатор приема пищи.
     * @param user Пользователь, которому принадлежит прием пищи.
     * @return Список объектов FoodEntryDTO.
     */
    List<FoodEntryDTO> getFoodEntriesByMeal(Integer mealId, User user);
    /**
     * Возвращает список приемов пищи с их записями о еде за указанную дату для пользователя.
     * @param user Пользователь, для которого запрашиваются приемы пищи.
     * @param date Дата, за которую запрашиваются приемы пищи.
     * @return Список объектов MealWithEntriesResponse.
     */
    List<MealWithEntriesResponse> getMealsWithFoodEntriesByDate(User user, LocalDate date);
    /**
     * Возвращает список приемов пищи за указанную дату для пользователя.
     * @param user Пользователь, для которого запрашиваются приемы пищи.
     * @param date Дата, за которую запрашиваются приемы пищи.
     * @return Список объектов MealDTO.
     */
    List<MealDTO> getMealsByDate(User user, LocalDate date);
    /**
     * Обновляет количество грамм порции для записи о еде.
     * @param entryId Идентификатор записи о еде.
     * @param newPortionGrams Новое количество грамм порции.
     * @param user Пользователь, которому принадлежит запись о еде.
     * @return Обновленный объект FoodEntryDTO.
     */
    FoodEntryDTO updateFoodEntryPortion(Integer entryId, BigDecimal newPortionGrams, User user);
    /**
     * Удаляет запись о еде.
     * @param entryId Идентификатор записи о еде.
     * @param user Пользователь, которому принадлежит запись о еде.
     */
    void deleteFoodEntry(Integer entryId, User user);
    /**
     * Удаляет прием пищи и все связанные с ним записи о еде.
     * @param mealId Идентификатор приема пищи.
     * @param user Пользователь, которому принадлежит прием пищи.
     */
    void deleteMeal(Integer mealId, User user);
}
