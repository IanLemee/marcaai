package com.tech.agendaai.company.controller;

import com.tech.agendaai.company.model.appointment.AppointmentResponse;
import com.tech.agendaai.company.service.appointment.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/hours")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/list")
    public List<LocalDateTime> availableSchedules() {
        return appointmentService.allAvailableSchedules();
    }

    @GetMapping("appointments/{nickname}/{name}")
    public ResponseEntity<List<AppointmentResponse>> appointments(@PathVariable String nickname, @PathVariable String name) {
        return new ResponseEntity<>(appointmentService.allAppointments(nickname, name), HttpStatus.OK);
    }
}
