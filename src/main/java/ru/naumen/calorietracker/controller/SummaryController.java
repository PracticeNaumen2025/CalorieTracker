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

@RestController
@RequestMapping("/api/summary")
public class SummaryController extends BaseController {
    private final SummaryService summaryService;

    @Autowired
    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @PostMapping("/day")
    public ResponseEntity<DaySummaryResponse> getDaySummary(Principal principal, @RequestBody DaySummaryRequest request){
        Integer userId = getUserIdFromPrincipal(principal);
        return ResponseEntity.ok(summaryService.getDaySummary(userId, request));
    }

    @PostMapping("/period")
    public ResponseEntity<List<DaySummaryResponse>> getSummaryByPeriod(Principal principal, @RequestBody PeriodSummaryRequest request){
        Integer userId = getUserIdFromPrincipal(principal);
        return ResponseEntity.ok(summaryService.getPeriodSummary(userId, request));
    }
}
