package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.naumen.calorietracker.model.FoodEntry;
import ru.naumen.calorietracker.model.Meal;
import ru.naumen.calorietracker.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface FoodEntryRepository extends JpaRepository<FoodEntry, Integer> {
    List<FoodEntry> findByMealUserAndMealDateTimeBetween(User user, LocalDateTime start, LocalDateTime end);
    List<FoodEntry> findByMealMealId(Integer mealId);
    List<FoodEntry> findByMealIn(List<Meal> meals);

    @Query("""
    SELECT p.category.categoryName, COUNT(fe.entryId)
    FROM FoodEntry fe
    JOIN fe.product p
    WHERE fe.meal.dateTime BETWEEN :from AND :to
    GROUP BY p.category.categoryName
    ORDER BY COUNT(fe.entryId) DESC
    """)
    List<Object[]> findPopularCategories(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("""
    SELECT fe.product.productId, fe.product.name,
           SUM(fe.calories), SUM(fe.proteinG), SUM(fe.fatG), SUM(fe.carbsG)
    FROM FoodEntry fe
    WHERE fe.meal.dateTime BETWEEN :from AND :to
    GROUP BY fe.product.productId, fe.product.name
    ORDER BY fe.product.name
    """)
    List<Object[]> findNutritionSummaryByProduct(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("""
    SELECT fe.product.productId, fe.product.name, AVG(fe.portionGrams)
    FROM FoodEntry fe
    WHERE fe.meal.dateTime BETWEEN :from AND :to
    GROUP BY fe.product.productId, fe.product.name
    ORDER BY fe.product.name
    """)
    List<Object[]> findAveragePortionSize(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("""
        SELECT fe.product.productId, fe.product.name, COUNT(fe.entryId) as usageCount
        FROM FoodEntry fe
        WHERE fe.meal.dateTime BETWEEN :from AND :to
        GROUP BY fe.product.productId, fe.product.name
        ORDER BY usageCount DESC
    """)
    List<Object[]> findPopularProducts(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("select coalesce(sum(f.calories), 0) from FoodEntry f where f.meal.dateTime between :start and :end")
    BigDecimal sumCaloriesBetweenDates(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
