package com.tech.agendaai.company.service.appointment;

import com.tech.agendaai.company.model.appointment.AppointmentRequest;
import com.tech.agendaai.company.model.appointment.AppointmentResponse;
import com.tech.agendaai.company.model.appointment.Appointments;
import com.tech.agendaai.company.model.appointment.Status;
import com.tech.agendaai.company.model.company.CompanyNotFoundException;
import com.tech.agendaai.company.model.interval.LunchIntervalResponse;
import com.tech.agendaai.company.model.operatingHours.OpenAndClose;
import com.tech.agendaai.company.repository.AppointmentRepository;
import com.tech.agendaai.company.service.*;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CompanyService companyService;
    private final ServicesService servicesService;
    private final OperatingHoursService operatingHoursService;
    private final UserService userService;
    private final CustomerService customerService;
    private final LunchIntervalService intervalService;


    public AppointmentService(AppointmentRepository appointmentRepository, CompanyService companyService, ServicesService servicesService, OperatingHoursService operatingHoursService, UserService userService, CustomerService customerService, LunchIntervalService intervalService) {
        this.appointmentRepository = appointmentRepository;
        this.companyService = companyService;
        this.servicesService = servicesService;
        this.operatingHoursService = operatingHoursService;
        this.userService = userService;
        this.customerService = customerService;
        this.intervalService = intervalService;
    }

    @CacheEvict(key = "#")
    public void createAppointment() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Appointments build = Appointments.builder()
                .scheduledTime(LocalDateTime.parse("", pattern))
                .company(companyService.findByNickname("").orElseThrow(CompanyNotFoundException::new))
                .user(userService.findUserBy("").orElseThrow())
                .customer(customerService.createCustomer(null))
                .services(servicesService.findServiceBy("").orElseThrow(RuntimeException::new))
                .status(Status.CONFIRMED)
                .build();
        save(build);
    }

    @Transactional
    private Appointments save(Appointments build) {
        return appointmentRepository.save(build);
    }

    //TODO
    // A LOT!
    public List<LocalDateTime> allAvailableSchedules() {
        int remainderDays = LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth();
        List<LocalDateTime> allAvailableHours = new ArrayList<>();
        LocalDate currentDay = LocalDate.now();
        int duration = 50;

        Set<Integer> days = operatingHoursService.workDays("agendaai-barber");
        while (remainderDays >= 0) {

            while (!days.contains(currentDay.getDayOfWeek().getValue())) {
                remainderDays--;
                currentDay = currentDay.plusDays(1);
            }
            OpenAndClose operatingHoursAt = operatingHoursService.getOperatingHoursAt(currentDay, "agendaai-barber");
            LocalTime open = operatingHoursAt.open();
            LocalTime close = operatingHoursAt.close();

            while (!open.plusMinutes(duration).isAfter(close)) {
                allAvailableHours.add(
                        LocalDateTime.of(
                                currentDay,
                                open
                        )
                );

                open = open.plusMinutes(duration);
            }

            currentDay = currentDay.plusDays(1);
            remainderDays--;
        }

        List<AppointmentResponse> allAppointmentResponses = appointmentRepository.findAllAppointments("", "");

        LocalDateTime now = LocalDateTime.now();
        int j = 0;
        while (allAvailableHours.get(j).isBefore(now)) {
            allAvailableHours.remove(j);
        }

        List<LocalDateTime> valuesToRemove = new ArrayList<>();


        for (AppointmentResponse appointmentResponse : allAppointmentResponses) {
            for (LocalDateTime dateTime : allAvailableHours) {
                if (isBetween(appointmentResponse, dateTime, duration)) {
                    valuesToRemove.add(dateTime);
                }
                if (dateTime.isAfter(appointmentResponse.scheduledTime())) break;
            }
        }

        allAvailableHours.removeAll(valuesToRemove);

        return allAvailableHours;
    }

    @Cacheable(value = "avaliableAppointments",key = "#request")
    public void allAvaliableAppointments(AppointmentRequest request) {
        List<LocalDateTime> allAvailableHours = allAvailableSchedules();
        Set<Integer> days = operatingHoursService.workDays(request.nickname());

        if (!days.contains(request.date().getDayOfWeek().getValue())) throw new IllegalArgumentException();

        List<AppointmentResponse> allAppointments = appointmentRepository.findAllAppointments(request.nickname(), request.name());
        OpenAndClose operatingHoursAt = operatingHoursService.getOperatingHoursAt(request.date(), request.nickname());

        LocalTime start = operatingHoursAt.open();
        LocalTime end = operatingHoursAt.close();
        LunchIntervalResponse interval = intervalService.getInterval(operatingHoursAt.interval_id());
        while(!start.equals(end)) {
            if (!start.isBefore(interval.start()) && !start.isAfter(interval.end())) {
                start = interval.end();
            }
            allAvailableHours.add(
                    LocalDateTime.of(request.date(), start)
            );
            start = start.plusMinutes(request.servicesRequest().duration());
        }

        for (LocalDateTime hours : allAvailableHours) {
            if (!hours.toLocalTime().isBefore(LocalTime.now())) {
                break;
            }
            allAvailableHours.remove(hours);
        }

//        List<LocalDateTime> valuesToRemove = new ArrayList<>();

        for (AppointmentResponse appointmentResponse : allAppointments) {
            for (LocalDateTime dateTime : allAvailableHours) {
                if (isBetween(appointmentResponse, dateTime, request.servicesRequest().duration())) {
//                    valuesToRemove.add(dateTime);
                    allAvailableHours.remove(dateTime);
                }
                if (dateTime.isAfter(appointmentResponse.scheduledTime())) break;
            }
        }

//        allAvailableHours.removeAll(valuesToRemove);
    }

    private static boolean isBetween(AppointmentResponse appointment, LocalDateTime dateTime, int duration) {
        return (!dateTime.isBefore(appointment.scheduledTime()) && !dateTime.isAfter(appointment.scheduledTime().plusMinutes(appointment.duration())) || (!dateTime.plusMinutes(duration).isBefore(appointment.scheduledTime()) && !dateTime.isAfter(appointment.scheduledTime().plusMinutes(appointment.duration()))));
    }

    public void deleteAppointments() {
        appointmentRepository.delete();
    }

    public List<AppointmentResponse> allAppointments(String nickname, String name) {
        return appointmentRepository.findAllAppointments(nickname, name);
    }

    public List<Appointments> allAppointments() {
        return appointmentRepository.findAppointments();
    }

    public void saveAll(List<Appointments> appointments) {
        appointmentRepository.saveAll(appointments);
    }
}
