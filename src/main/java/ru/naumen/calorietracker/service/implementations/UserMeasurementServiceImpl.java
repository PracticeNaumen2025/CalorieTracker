package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.naumen.calorietracker.dto.UserMeasurementResponse;
import ru.naumen.calorietracker.mapper.UserMeasurementMapper;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.model.UserMeasurement;
import ru.naumen.calorietracker.repository.UserMeasurementRepository;
import ru.naumen.calorietracker.repository.UserRepository;
import ru.naumen.calorietracker.service.UserMeasurementService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMeasurementServiceImpl implements UserMeasurementService {

    private final UserMeasurementRepository userMeasurementRepository;
    private final UserRepository userRepository;
    private final UserMeasurementMapper userMeasurementMapper;

    @Override
    @Transactional
    public void saveDailyUserMeasurements(LocalDate date) {
        List<User> users = userRepository.findAllActiveUsers();

        List<UserMeasurement> measurementsToday = userMeasurementRepository.findAllByDate(date);

        Map<Integer, UserMeasurement> measurementByUserId = measurementsToday.stream()
                .collect(Collectors.toMap(m -> m.getUser().getUserId(), m -> m));

        List<UserMeasurement> toSave = new ArrayList<>();

        for (User user : users) {
            BigDecimal height = user.getHeightCm();
            BigDecimal weight = user.getWeightKg();

            if (height == null && weight == null) continue;

            UserMeasurement measurement = measurementByUserId.get(user.getUserId());

            if (measurement != null) {
                boolean changed = false;
                if (height != null && !height.equals(measurement.getHeightCm())) {
                    measurement.setHeightCm(height);
                    changed = true;
                }
                if (weight != null && !weight.equals(measurement.getWeightKg())) {
                    measurement.setWeightKg(weight);
                    changed = true;
                }
                if (changed) toSave.add(measurement);
            } else {
                UserMeasurement newMeasurement = new UserMeasurement();
                newMeasurement.setUser(user);
                newMeasurement.setDate(date);
                newMeasurement.setHeightCm(height);
                newMeasurement.setWeightKg(weight);
                toSave.add(newMeasurement);
            }
        }

        if (!toSave.isEmpty()) {
            userMeasurementRepository.saveAll(toSave);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserMeasurementResponse> getAllMeasurementsByUser(User user) {
        List<UserMeasurement> measurements = userMeasurementRepository.findAllByUserOrderByDateAsc(user);
        return userMeasurementMapper.toResponseList(measurements);
    }
}

