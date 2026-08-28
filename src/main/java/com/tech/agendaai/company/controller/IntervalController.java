package com.tech.agendaai.company.controller;

import com.tech.agendaai.company.model.interval.ChangeIntervalRequest;
import com.tech.agendaai.company.model.interval.CreateIntervalResponse;
import com.tech.agendaai.company.model.interval.IntervalCreateRequest;
import com.tech.agendaai.company.service.EmployeeIntervalsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/interval")
public class IntervalController {

    private final EmployeeIntervalsService intervalsService;

    public IntervalController(EmployeeIntervalsService intervalsService) {
        this.intervalsService = intervalsService;
    }

    @PostMapping
    public ResponseEntity<CreateIntervalResponse> createInterval(IntervalCreateRequest request) {
        CreateIntervalResponse interval = intervalsService.createInterval(request);
        return new ResponseEntity<>(interval, HttpStatus.OK);
    }

    @PutMapping("/change/intervals/{id}")
    public ResponseEntity<Void> changeIntervals(@PathVariable Long id, ChangeIntervalRequest request) {
        intervalsService.changeInterval(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
