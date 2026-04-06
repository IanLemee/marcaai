package com.tech.agendaai.company.controller;

import com.tech.agendaai.company.model.operatingHours.ChangeOperatingHour;
import com.tech.agendaai.company.model.operatingHours.CreateOperatingHoursRequest;
import com.tech.agendaai.company.service.OperatingHoursService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operating")
public class OperatingHoursController {

    private final OperatingHoursService operatingHoursService;

    public OperatingHoursController(OperatingHoursService operatingHoursService) {
        this.operatingHoursService = operatingHoursService;
    }


    @PostMapping("/create")
    public void create(@RequestBody List<CreateOperatingHoursRequest> request) {
        operatingHoursService.create(request);
    }

    @PatchMapping("/")
    public void changeOperatingHour(ChangeOperatingHour date) {
        operatingHoursService.changeOperatingHour(date);
    }
}
