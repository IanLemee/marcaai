package com.tech.agendaai.company.controller;

import com.tech.agendaai.company.documentation.AppointmentControllerDocs;
import com.tech.agendaai.company.model.appointment.*;
import com.tech.agendaai.company.service.appointment.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/hours")
public class AppointmentController implements AppointmentControllerDocs {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping(value = "/create/{companyNickname}")
    @Override
    public ResponseEntity<AppointmentResponse> createAppointment(CreateAppointment appointment, @PathVariable String companyNickname) {
        AppointmentResponse res = appointmentService.createAppointment(appointment, companyNickname);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<AppointmentDto>> availableSchedules(
            @RequestParam String companyNickname,
            @RequestParam String date,
            @RequestParam(required = false) String name,
            @RequestParam String service,
            @RequestParam int duration
    ) {
        ServicesRequest servicesRequest = new ServicesRequest(service, duration);
        AppointmentRequest request = new AppointmentRequest(date, companyNickname, name, servicesRequest);

        List<AppointmentDto> res = appointmentService.allAvailableAppointments(request);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    // Polling
    @GetMapping("/appointments/{company}")
    public ResponseEntity<List<AppointmentsAvailable>> appointments(@PathVariable String company) {
        return new ResponseEntity<>(appointmentService.appointmentsForToday(company), HttpStatus.OK);
    }

    @PatchMapping("/change/date/{appointmentId}")
    public ResponseEntity<Void> changeDate(@PathVariable String appointmentId, LocalDateTime date) {
        appointmentService.changeDate(appointmentId, date);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // deveria ser idempotente?
    @DeleteMapping("/cancel/{appointmentId}/{customerId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable String appointmentId, @PathVariable String customerId) {
        appointmentService.cancelAppointment(appointmentId, customerId);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        appointmentService.addEmitter(sseEmitter);
        return sseEmitter;
    }
}
