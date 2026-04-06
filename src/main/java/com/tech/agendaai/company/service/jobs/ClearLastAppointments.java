package com.tech.agendaai.company.service.jobs;

import com.tech.agendaai.company.model.appointment.Appointments;
import com.tech.agendaai.company.model.appointment.Status;
import com.tech.agendaai.company.service.appointment.AppointmentService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableAsync
public class ClearLastAppointments {
    private final AppointmentService appointmentService;

    public ClearLastAppointments(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Scheduled(cron = "0 0 22 * * Sun", zone = "America/Sao_Paulo")
    public void deleteData() {
        appointmentService.deleteAppointments();
    }

    @Scheduled(fixedRate = 100000)
    @Async
    public void updateStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Appointments> list = appointmentService.allAppointments().stream().filter(appointments ->
                !now.isBefore(appointments.getScheduledTime()) && !now.isAfter(appointments.getScheduledTime().plusMinutes(appointments.getServices().getDuration()))
        ).peek(appointments -> appointments.setStatus(Status.IN_PROGRESS))
        .toList();

        if (list.isEmpty()) return;
        appointmentService.saveAll(list);
    }
}
