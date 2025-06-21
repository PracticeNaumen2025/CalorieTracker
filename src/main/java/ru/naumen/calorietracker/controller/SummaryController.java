package ru.naumen.calorietracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.naumen.calorietracker.dto.DaySummaryRequest;
import ru.naumen.calorietracker.dto.DaySummaryResponse;
import ru.naumen.calorietracker.dto.PeriodSummaryRequest;
import ru.naumen.calorietracker.service.SummaryService;

import java.util.List;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {
    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/day")
    public ResponseEntity<DaySummaryResponse> getDaySummary(DaySummaryRequest request){
        return ResponseEntity.ok(summaryService.getDaySummary(request));
    }

    @GetMapping("/period")
    public ResponseEntity<List<DaySummaryResponse>> getSummaryByPeriod(PeriodSummaryRequest request){
        return ResponseEntity.ok(summaryService.getPeriodSummary(request));
    }
}
