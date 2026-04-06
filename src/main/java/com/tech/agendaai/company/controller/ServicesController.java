package com.tech.agendaai.company.controller;

import com.tech.agendaai.company.model.services.CreateServicesRequest;
import com.tech.agendaai.company.service.ServicesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServicesController {

    private final ServicesService servicesService;

    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @PostMapping("/create")
    public void createService(@RequestBody List<CreateServicesRequest> request) {
        servicesService.createService(request);
    }
}
