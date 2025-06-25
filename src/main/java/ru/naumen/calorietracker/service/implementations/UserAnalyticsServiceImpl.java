package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.naumen.calorietracker.dto.*;
import ru.naumen.calorietracker.model.Role;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.repository.ExerciseEntryRepository;
import ru.naumen.calorietracker.repository.MealRepository;
import ru.naumen.calorietracker.repository.UserRepository;
import ru.naumen.calorietracker.service.UserAnalyticsService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAnalyticsServiceImpl implements UserAnalyticsService {

    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final ExerciseEntryRepository exerciseEntryRepository;

    @Override
    public UserStatisticsResponse getUserStatistics() {
        List<User> users = userRepository.findAll();

        long total = users.size();
        long active = userRepository.countByIsDeletedFalse();
        long deleted = userRepository.countByIsDeletedTrue();
        Map<String, Long> roles = users.stream()
                .flatMap(u -> u.getRoles().stream())
                .collect(Collectors.groupingBy(Role::getRoleName, Collectors.counting()));

        UserStatisticsResponse response = new UserStatisticsResponse();
        response.setTotalUsers(total);
        response.setActiveUsers(active);
        response.setDeletedUsers(deleted);
        response.setUsersByRole(roles);
        return response;
    }

    @Override
    public UserDemographicsResponse getUserDemographics() {
        List<User> users = userRepository.findByIsDeletedFalse();

        Map<String, Long> genderDistribution = users.stream()
                .filter(u -> u.getGender() != null)
                .collect(Collectors.groupingBy(User::getGender, Collectors.counting()));

        double averageAge = users.stream()
                .filter(u -> u.getDateOfBirth() != null)
                .mapToInt(u -> LocalDate.now().getYear() - u.getDateOfBirth().getYear())
                .average().orElse(0);

        Map<String, Long> ageGroups = new HashMap<>();
        ageGroups.put("18-25", 0L);
        ageGroups.put("26-35", 0L);
        ageGroups.put("36-50", 0L);
        ageGroups.put("50+", 0L);

        for (User u : users) {
            if (u.getDateOfBirth() == null) continue;
            int age = LocalDate.now().getYear() - u.getDateOfBirth().getYear();
            if (age < 26) ageGroups.compute("18-25", (k, v) -> (v == null ? 1L : v + 1));
            else if (age < 36) ageGroups.compute("26-35", (k, v) -> (v == null ? 1L : v + 1));
            else if (age < 51) ageGroups.compute("36-50", (k, v) -> (v == null ? 1L : v + 1));
            else ageGroups.compute("50+", (k, v) -> (v == null ? 1L : v + 1));
        }

        BigDecimal avgHeight = users.stream()
                .map(User::getHeightCm)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(users.size()), 2, RoundingMode.HALF_UP);

        BigDecimal avgWeight = users.stream()
                .map(User::getWeightKg)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(users.size()), 2, RoundingMode.HALF_UP);

        UserDemographicsResponse response = new UserDemographicsResponse();
        response.setGenderDistribution(genderDistribution);
        response.setAverageAge(averageAge);
        response.setAgeGroups(ageGroups);
        response.setAverageHeightCm(avgHeight);
        response.setAverageWeightKg(avgWeight);
        return response;
    }

    @Override
    public List<UserRegistrationStatsResponse> getRegistrationStats(LocalDate from, LocalDate to) {
        List<Object[]> rows = userRepository.countRegistrationsByDateRange(from.atStartOfDay(), to.atTime(23, 59));
        return rows.stream()
                .map(r -> new UserRegistrationStatsResponse(
                        ((java.sql.Date) r[0]).toLocalDate(), (Long) r[1]
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ActiveUsersResponse getActiveUsers(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);

        Set<Integer> activeUserIds = new HashSet<>(mealRepository.findActiveUserIdsSince(since));
        activeUserIds.addAll(exerciseEntryRepository.findActiveUserIdsSince(since));
        long totalActiveUsers = activeUserIds.size();

        ActiveUsersResponse response = new ActiveUsersResponse();
        response.setActiveUsersCount(totalActiveUsers);
        return response;
    }
}

