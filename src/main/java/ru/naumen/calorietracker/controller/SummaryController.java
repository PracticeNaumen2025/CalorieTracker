package ru.naumen.calorietracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.naumen.calorietracker.dto.DaySummaryRequest;
import ru.naumen.calorietracker.dto.DaySummaryResponse;
import ru.naumen.calorietracker.dto.PeriodSummaryRequest;
import ru.naumen.calorietracker.service.SummaryService;

import java.security.Principal;
import java.util.List;

/**
 * REST-контроллер для получения агрегированных дневных и периодических сводок
 * по потреблению калорий и нутриентов пользователя.
 * <p>
 * Используется для получения суммарных данных по калориям, белкам, жирам и углеводам
 * за конкретный день или за период.
 */
@RestController
@RequestMapping("/api/summary")
public class SummaryController extends BaseController {
    private final SummaryService summaryService;

    @Autowired
    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    /**
     * Получить агрегированную сводку по дате для аутентифицированного пользователя.
     * Включает суммарные данные по калориям и нутриентам за один день.
     *
     * @param principal объект аутентификации пользователя
     * @param request   запрос с датой (yyyy-MM-dd), за которую нужна сводка
     * @return объект с агрегированными данными по выбранному дню
     */
    @PostMapping("/day")
    public ResponseEntity<DaySummaryResponse> getDaySummary(Principal principal, @RequestBody DaySummaryRequest request){
        Integer userId = getUserIdFromPrincipal(principal);
        return ResponseEntity.ok(summaryService.getDaySummary(userId, request));
    }

    /**
     * Получить список агрегированных сводок по дням за указанный период для аутентифицированного пользователя.
     * Каждая сводка включает суммарные калории и нутриенты за соответствующий день.
     *
     * @param principal объект аутентификации пользователя
     * @param request   запрос с датами начала и конца периода (yyyy-MM-dd)
     * @return список дневных сводок за период
     */
    @PostMapping("/period")
    public ResponseEntity<List<DaySummaryResponse>> getSummaryByPeriod(Principal principal, @RequestBody PeriodSummaryRequest request){
        Integer userId = getUserIdFromPrincipal(principal);
        return ResponseEntity.ok(summaryService.getPeriodSummary(userId, request));
    }
}
