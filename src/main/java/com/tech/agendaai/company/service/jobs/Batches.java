package com.tech.agendaai.company.service.jobs;

import com.tech.agendaai.company.model.appointment.Appointments;
import com.tech.agendaai.company.model.appointment.Status;
import com.tech.agendaai.company.model.credits.CompanyCredits;
import com.tech.agendaai.company.service.CompanyCreditsService;
import com.tech.agendaai.company.service.CustomerService;
import com.tech.agendaai.company.service.appointment.AppointmentService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class Batches {
    private final AppointmentService appointmentService;
    private final CustomerService customerService;
    private final CompanyCreditsService companyCreditsService;

    public Batches(AppointmentService appointmentService, CustomerService customerService, CompanyCreditsService companyCreditsService) {
        this.appointmentService = appointmentService;
        this.customerService = customerService;
        this.companyCreditsService = companyCreditsService;
    }

    /**
     * DeleteAppointments deletes all appointments with status done or canceled at the end of the week or phone number null
     */
    @Scheduled(cron = "0 0 22 * * Sun", zone = "America/Sao_Paulo")
    public void deleteAppointments() {
        appointmentService.deleteAppointments();
    }

    @Scheduled(cron = "0 59 23 * * *", zone = "America/Sao_Paulo")
    public void deleteCustomersWithoutPhoneNumber() {
        customerService.deleteNullNumbers();
    }

    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void updateStatusToProgress() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(LocalTime.MAX);
        List<Appointments> appointments = appointmentService.appointmentsForToday(startOfDay, endOfDay);
        if (appointments.isEmpty()) return;

        appointments.stream()
                .filter(appointment ->
                        !now.isBefore(appointment.getScheduledTime()) && !now.isAfter(appointment.getScheduledTime().plusMinutes(appointment.getServices().getDuration())))
                .forEach(appointment ->
                        appointment.setStatus(Status.IN_PROGRESS)
                );
    }

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void updateStatusToDone() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(LocalTime.MAX);
        long id = 0;

        List<Appointments> appointments = appointmentService.appointmentsForToday(startOfDay, endOfDay, id);
        if (appointments.isEmpty()) return;
        
        appointments.stream()
                .filter(appointment -> appointment.getScheduledTime().isBefore(now))
                .forEach(appointment ->
                        appointment.setStatus(Status.DONE)
                );
    }

    @Scheduled(cron = "0 0 00 1 * *", zone = "America/Sao_Paulo")
    public void updateDefaultCredits() {
        long id = 0L;

        Optional<List<CompanyCredits>> listCredits = companyCreditsService.getNextCompanies(id);

        int idx = 0;
        while (listCredits.isPresent() && idx < listCredits.get().size()) {
            List<CompanyCredits> credits = listCredits.get();

            while (idx < credits.size()) {
                credits.get(idx++).setDefaultCredits(BigDecimal.valueOf(100));
            }
            id = idx + 1L;
            listCredits = companyCreditsService.getNextCompanies(id);
        }
    }
}
