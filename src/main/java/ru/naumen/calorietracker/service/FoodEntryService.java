package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.FoodEntryDTO;
import ru.naumen.calorietracker.dto.ProductResponse;
import ru.naumen.calorietracker.model.FoodEntry;
import ru.naumen.calorietracker.model.Meal;
import ru.naumen.calorietracker.model.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервис для работы с записями о еде.
 */
public interface FoodEntryService {
    /**
     * Добавляет новую запись о еде.
     * @param meal Прием пищи, к которому относится запись.
     * @param productResponse Продукт, который был употреблен.
     * @param portionGrams Количество грамм продукта.
     * @return Объект FoodEntryDTO, представляющий добавленную запись о еде.
     */
    FoodEntryDTO addFoodEntry(Meal meal, ProductResponse productResponse, BigDecimal portionGrams);
    /**
     * Возвращает список записей о еде для указанного приема пищи и пользователя.
     * @param mealId Идентификатор приема пищи.
     * @param user Пользователь, которому принадлежит прием пищи.
     * @return Список объектов FoodEntryDTO.
     */
    List<FoodEntryDTO> getFoodEntriesByMealId(Integer mealId, User user);
    /**
     * Возвращает список записей о еде для указанных приемов пищи.
     * @param meals Список приемов пищи.
     * @return Список объектов FoodEntry.
     */
    List<FoodEntry> getFoodEntriesByMeals(List<Meal> meals);
    /**
     * Обновляет количество грамм порции для записи о еде.
     * @param entryId Идентификатор записи о еде.
     * @param newPortionGrams Новое количество грамм порции.
     * @param user Пользователь, которому принадлежит запись о еде.
     * @return Обновленный объект FoodEntryDTO.
     */
    FoodEntryDTO updatePortionGrams(Integer entryId, BigDecimal newPortionGrams, User user);
    /**
     * Удаляет запись о еде.
     * @param entryId Идентификатор записи о еде.
     * @param user Пользователь, которому принадлежит запись о еде.
     */
    void deleteFoodEntry(Integer entryId, User user);
    /**
     * Возвращает запись о еде по ее идентификатору.
     * @param entryId Идентификатор записи о еде.
     * @return Объект FoodEntry.
     */
    FoodEntry getFoodEntryById(Integer entryId);
}