package ru.naumen.calorietracker.service.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.naumen.calorietracker.dto.*;
import ru.naumen.calorietracker.model.Role;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.repository.ExerciseEntryRepository;
import ru.naumen.calorietracker.repository.MealRepository;
import ru.naumen.calorietracker.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAnalyticsServiceImplTest {

    private UserRepository userRepository;
    private MealRepository mealRepository;
    private ExerciseEntryRepository exerciseEntryRepository;
    private UserAnalyticsServiceImpl service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        mealRepository = mock(MealRepository.class);
        exerciseEntryRepository = mock(ExerciseEntryRepository.class);
        service = new UserAnalyticsServiceImpl(userRepository, mealRepository, exerciseEntryRepository);
    }

    @Test
    void getUserStatistics_returnsCorrectCountsAndRoles() {
        Role roleUser = new Role();
        roleUser.setRoleName("USER");
        Role roleAdmin = new Role();
        roleAdmin.setRoleName("ADMIN");

        User u1 = new User();
        u1.setRoles(Set.of(roleUser));
        User u2 = new User();
        u2.setRoles(Set.of(roleUser, roleAdmin));
        User u3 = new User();
        u3.setRoles(Set.of(roleAdmin));

        List<User> allUsers = List.of(u1, u2, u3);

        when(userRepository.findAll()).thenReturn(allUsers);
        when(userRepository.countByIsDeletedFalse()).thenReturn(2L);
        when(userRepository.countByIsDeletedTrue()).thenReturn(1L);

        UserStatisticsResponse response = service.getUserStatistics();

        assertEquals(3, response.getTotalUsers());
        assertEquals(2, response.getActiveUsers());
        assertEquals(1, response.getDeletedUsers());
        assertEquals(2L, response.getUsersByRole().get("USER"));
        assertEquals(2L, response.getUsersByRole().get("ADMIN"));
    }

    @Test
    void getUserDemographics_calculatesCorrectly() {
        User u1 = new User();
        u1.setGender("Male");
        u1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        u1.setHeightCm(new BigDecimal("180"));
        u1.setWeightKg(new BigDecimal("75"));

        User u2 = new User();
        u2.setGender("Female");
        u2.setDateOfBirth(LocalDate.of(1980, 1, 1));
        u2.setHeightCm(new BigDecimal("160"));
        u2.setWeightKg(new BigDecimal("60"));

        User u3 = new User();
        u3.setGender("Male");
        u3.setDateOfBirth(null); // no birthdate, ignored in averages
        u3.setHeightCm(null);
        u3.setWeightKg(null);

        List<User> users = List.of(u1, u2, u3);

        when(userRepository.findByIsDeletedFalse()).thenReturn(users);

        UserDemographicsResponse response = service.getUserDemographics();

        // Gender distribution
        assertEquals(2L, response.getGenderDistribution().get("Male"));
        assertEquals(1L, response.getGenderDistribution().get("Female"));

        // Average age approx (2025 - 1990 + 2025 - 1980)/2 = (35 + 45)/2 = 40
        assertTrue(response.getAverageAge() > 39 && response.getAverageAge() < 41);

        // Age groups counts
        // 1990 -> 35 years (26-35)
        // 1980 -> 45 years (36-50)
        assertEquals(0L, response.getAgeGroups().get("18-25"));
        assertEquals(1L, response.getAgeGroups().get("26-35"));
        assertEquals(1L, response.getAgeGroups().get("36-50"));
        assertEquals(0L, response.getAgeGroups().get("50+"));

        // Average height = (180 + 160) / 3 = 113.33 rounded to 2 decimals
        assertEquals(new BigDecimal("113.33"), response.getAverageHeightCm());

        // Average weight = (75 + 60) / 3 = 45.00
        assertEquals(new BigDecimal("45.00"), response.getAverageWeightKg());
    }

    @Test
    void getRegistrationStats_mapsResults() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 3);

        java.sql.Date sqlDate1 = java.sql.Date.valueOf(LocalDate.of(2025, 6, 1));
        java.sql.Date sqlDate2 = java.sql.Date.valueOf(LocalDate.of(2025, 6, 2));

        List<Object[]> repoResult = List.of(
                new Object[]{sqlDate1, 5L},
                new Object[]{sqlDate2, 7L}
        );

        when(userRepository.countRegistrationsByDateRange(
                any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(repoResult);

        List<UserRegistrationStatsResponse> response = service.getRegistrationStats(from, to);

        assertEquals(2, response.size());
        assertEquals(LocalDate.of(2025, 6, 1), response.get(0).getDate());
        assertEquals(5L, response.get(0).getCount());
        assertEquals(LocalDate.of(2025, 6, 2), response.get(1).getDate());
        assertEquals(7L, response.get(1).getCount());
    }

    @Test
    void getActiveUsers_combinesUniqueUserIds() {
        LocalDateTime since = LocalDateTime.now().minusDays(7);

        when(mealRepository.findActiveUserIdsSince(any(LocalDateTime.class)))
                .thenReturn(List.of(1, 2, 3));
        when(exerciseEntryRepository.findActiveUserIdsSince(any(LocalDateTime.class)))
                .thenReturn(List.of(3, 4, 5));

        ActiveUsersResponse response = service.getActiveUsers(7);

        assertEquals(5, response.getActiveUsersCount());
    }
}
