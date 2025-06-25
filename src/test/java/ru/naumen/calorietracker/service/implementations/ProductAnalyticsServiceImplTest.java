package ru.naumen.calorietracker.service.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.naumen.calorietracker.dto.*;
import ru.naumen.calorietracker.repository.FoodEntryRepository;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductAnalyticsServiceImplTest {

    private FoodEntryRepository foodEntryRepository;
    private ProductAnalyticsServiceImpl service;

    @BeforeEach
    void setUp() {
        foodEntryRepository = mock(FoodEntryRepository.class);
        service = new ProductAnalyticsServiceImpl(foodEntryRepository);
    }

    @Test
    void getPopularProducts_returnsMappedList() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 10);

        when(foodEntryRepository.findPopularProducts(any(), any()))
                .thenReturn(List.of(
                        new Object[]{1, "Яблоко", 12L},
                        new Object[]{2, "Хлеб", 8L}
                ));

        List<ProductPopularityResponse> result = service.getPopularProducts(from, to, 2);

        assertEquals(2, result.size());
        assertEquals("Яблоко", result.getFirst().getProductName());
        assertEquals(12L, result.getFirst().getUsageCount());
    }

    @Test
    void getAveragePortionSizes_returnsMappedList() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 10);

        when(foodEntryRepository.findAveragePortionSize(any(), any()))
                .thenReturn(List.of(
                        new Object[]{1, "Каша", 120.5},
                        new Object[]{2, "Молоко", 200.0}
                ));

        List<ProductAveragePortionResponse> result = service.getAveragePortionSizes(from, to);

        assertEquals(2, result.size());
        assertEquals("Каша", result.getFirst().getProductName());
        assertEquals(120.5, result.getFirst().getAveragePortionGrams());
    }


    @Test
    void getPopularCategories_returnsMappedList() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 10);

        when(foodEntryRepository.findPopularCategories(any(), any()))
                .thenReturn(List.of(
                        new Object[]{"Фрукты", 20L},
                        new Object[]{"Молочные", 10L}
                ));

        List<CategoryPopularityResponse> result = service.getPopularCategories(from, to);

        assertEquals(2, result.size());
        assertEquals("Фрукты", result.get(0).getCategoryName());
        assertEquals(20L, result.get(0).getUsageCount());
    }

    @Test
    void getAverageDailyCaloriesConsumed_returnsCorrectAverage() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 3); // 3 дня

        when(foodEntryRepository.sumCaloriesBetweenDates(any(), any()))
                .thenReturn(BigDecimal.valueOf(6000));

        BigDecimal result = service.getAverageDailyCaloriesConsumed(from, to);

        assertEquals(BigDecimal.valueOf(2000.00).setScale(2), result);
    }

    @Test
    void getAverageDailyCaloriesConsumed_handlesInvalidRange() {
        LocalDate from = LocalDate.of(2025, 6, 5);
        LocalDate to = LocalDate.of(2025, 6, 3); // to < from

        BigDecimal result = service.getAverageDailyCaloriesConsumed(from, to);

        assertEquals(BigDecimal.ZERO, result);
        verify(foodEntryRepository, never()).sumCaloriesBetweenDates(any(), any());
    }
}
