package ru.naumen.calorietracker.service.implementations;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.naumen.calorietracker.dto.PageResponse;
import ru.naumen.calorietracker.dto.UserGoalCreateRequest;
import ru.naumen.calorietracker.dto.UserGoalResponse;
import ru.naumen.calorietracker.dto.UserGoalUpdateRequest;
import ru.naumen.calorietracker.exception.DateOverlapException;
import ru.naumen.calorietracker.exception.EntityNotFoundException;
import ru.naumen.calorietracker.mapper.PageResponseMapper;
import ru.naumen.calorietracker.mapper.UserGoalMapper;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.model.UserGoal;
import ru.naumen.calorietracker.repository.UserGoalRepository;
import ru.naumen.calorietracker.repository.UserRepository;
import ru.naumen.calorietracker.service.UserGoalService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserGoalServiceImpl implements UserGoalService {

    private final UserGoalMapper userGoalMapper;
    private final UserGoalRepository userGoalRepository;
    private final AccessChecker accessChecker;
    private final UserRepository userRepository;

    public UserGoalServiceImpl(UserGoalMapper userGoalMapper, UserGoalRepository userGoalRepository, AccessChecker accessChecker, UserRepository userRepository) {
        this.userGoalMapper = userGoalMapper;
        this.userGoalRepository = userGoalRepository;
        this.accessChecker = accessChecker;
        this.userRepository = userRepository;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userGoals", key = "#userId"),
            @CacheEvict(value = "userActiveGoal", key = "#userId")
    })
    public UserGoalResponse createUserGoal(UserGoalCreateRequest request, Integer userId) {
        accessChecker.checkAccess(request.userId(), userId);
        User user = userRepository.findById(request.userId())
                .orElseThrow(()->
                        new EntityNotFoundException("Пользователь с id %s не найден"
                                .formatted(request.userId())));
        if(isOverlappingGoals(request.userId(), request.startDate(), request.endDate())){
            throw new DateOverlapException("Период цели пересекается с существующими целями");
        }
        UserGoal userGoal = userGoalMapper.toEntity(request);
        userGoal.setUser(user);
        UserGoal savedUserGoal = userGoalRepository.save(userGoal);
        return userGoalMapper.toResponse(savedUserGoal);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userGoals", key = "#userId"),
            @CacheEvict(value = "userActiveGoal", key = "#userId")
    })
    public UserGoalResponse updateUserGoal(UserGoalUpdateRequest request, Integer userId) {

        UserGoal userGoal = userGoalRepository.findById(request.goalId())
                .orElseThrow(()->
                        new EntityNotFoundException("Цель с id %s не найдена"
                                .formatted(request.goalId())));
        accessChecker.checkAccess(userGoal.getUser().getUserId(), userId);
        if(isOverlappingGoals(userId, request.startDate(), request.endDate())){
            throw new DateOverlapException("Период цели пересекается с существующими целями");
        }
        userGoal.setDailyCalorieGoal(request.dailyCalorieGoal());
        userGoal.setStartDate(request.startDate());
        userGoal.setEndDate(request.endDate());
        userGoal.setTargetWeightKg(BigDecimal.valueOf(request.targetWeightKg()));
        userGoal.setProteinPercentage(BigDecimal.valueOf(request.proteinPercentage()));
        userGoal.setFatPercentage(BigDecimal.valueOf(request.fatPercentage()));
        userGoal.setCarbPercentage(BigDecimal.valueOf(request.carbPercentage()));

        UserGoal savedGoal = userGoalRepository.save(userGoal);
        return userGoalMapper.toResponse(savedGoal);
    }

    @Override
    @Cacheable(value = "userActiveGoal", key = "#ownerUserId")
    public UserGoalResponse getUserGoalByUserIdAndDate(Integer ownerUserId, Integer currentUserId, LocalDate date) {
        accessChecker.checkAccess(ownerUserId, currentUserId);
        UserGoal goal = userGoalRepository.findActiveGoalByDate(ownerUserId, date)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "У пользователя с id=%s нет цели на дату %s"
                                        .formatted(ownerUserId, date)
                        )
                );
        return userGoalMapper.toResponse(goal);
    }

    @Override
    public PageResponse<UserGoalResponse> getUserGoalsByUserId(
            Integer ownerUserId,
            Integer currentUserId,
            Pageable pageable
    ) {
        accessChecker.checkAccess(ownerUserId, currentUserId);
        Page<UserGoal> goalsPage = userGoalRepository.findByUserUserIdOrderByStartDateAsc(ownerUserId, pageable);
        Page<UserGoalResponse> responsePage = goalsPage.map(userGoalMapper::toResponse);

        return PageResponseMapper.toPageResponse(responsePage);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userGoals", key = "#userId"),
            @CacheEvict(value = "userActiveGoal", key = "#userId")
    })
    public void deleteUserGoalByUserId(Integer userId, Integer goalId) {
        UserGoal goal = userGoalRepository.findById(goalId)
                .orElseThrow(()->
                        new EntityNotFoundException("Цель с id %s не найдена"
                                .formatted(goalId)));
        accessChecker.checkAccess(goal.getUser().getUserId(), userId);
        userGoalRepository.delete(goal);
    }

    private boolean isOverlappingGoals(Integer userId, LocalDate startDate, LocalDate endDate){
        return !userGoalRepository.findOverlappingGoals(userId, startDate, endDate).isEmpty();
    }
}
