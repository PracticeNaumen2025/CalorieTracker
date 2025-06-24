package ru.naumen.calorietracker.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.naumen.calorietracker.service.UserMeasurementService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMeasurementScheduler {

    private final UserMeasurementService userMeasurementService;

    // Запуск каждый день в 23:59
    @Scheduled(cron = "0 59 23 * * *")
    public void saveDailyUserMeasurementsScheduler() {
        log.info("Scheduler started running saveDailyUserMeasurements at {}", LocalDateTime.now());
        userMeasurementService.saveDailyUserMeasurements(LocalDate.now());
    }

    // Запуск при старте приложения
    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup() {
        log.info("Running saveDailyUserMeasurements once at application startup");
        saveDailyUserMeasurementsScheduler();
    }
}



