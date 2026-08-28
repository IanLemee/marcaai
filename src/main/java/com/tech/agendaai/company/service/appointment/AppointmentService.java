package com.tech.agendaai.company.service.appointment;

import com.github.f4b6a3.uuid.UuidCreator;
import com.tech.agendaai.company.model.appointment.*;
import com.tech.agendaai.company.model.company.Company;
import com.tech.agendaai.company.model.company.CompanyNotFoundException;
import com.tech.agendaai.company.model.company.Plan;
import com.tech.agendaai.company.model.customer.Customer;
import com.tech.agendaai.company.model.operatingHours.OperatingDayDontExistException;
import com.tech.agendaai.company.model.services.Services;
import com.tech.agendaai.company.model.user.Employees;
import com.tech.agendaai.company.model.user.User;
import com.tech.agendaai.company.repository.AppointmentRepository;
import com.tech.agendaai.company.service.*;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CompanyService companyService;
    private final ServicesService servicesService;
    private final OperatingHoursService operatingHoursService;
    private final UserService userService;
    private final CustomerService customerService;
    private final EmployeeIntervalsService intervalService;

    //TODO refactor method allAvailableHours, it's Request DTO and availableDays

    private final ConcurrentHashMap<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    public AppointmentService(AppointmentRepository appointmentRepository, CompanyService companyService, ServicesService servicesService, OperatingHoursService operatingHoursService, UserService userService, CustomerService customerService, EmployeeIntervalsService intervalService) {
        this.appointmentRepository = appointmentRepository;
        this.companyService = companyService;
        this.servicesService = servicesService;
        this.operatingHoursService = operatingHoursService;
        this.userService = userService;
        this.customerService = customerService;
        this.intervalService = intervalService;
    }

    // TODO whenever a user create an appointment a checkout is also created, SO, I need here to send a request to abacate pay to create the checkout here,
    // Use public ID as external ID, whenever it's comes back we confirm
    public AppointmentResponse createAppointment(CreateAppointment appointment, String companyNickname) {
        Company company = companyService.findByNickname(companyNickname).orElseThrow(CompanyNotFoundException::new);
        User user = userService.findUserByPublicId(appointment.employeeId()).orElseThrow();
        Customer customer = customerService.findByPublicId(appointment.customerPhoneN()).orElseThrow();
        Services service = servicesService.findServiceById(appointment.serviceId()).orElseThrow(RuntimeException::new);

        Appointments build = Appointments.builder()
                .scheduledTime(appointment.date())
                .company(company)
                .user(user)
                .customer(customer)
                .services(service)
                .status(Status.PENDING)
                .publicId(UuidCreator.getTimeOrderedEpoch())
                .build();

        try {
            Appointments saved = appointmentRepository.save(build);
            return new AppointmentResponse(saved.getPublicId().toString(), saved.getUser().getName(), saved.getScheduledTime());
        } catch (Exception e) {
            throw new AppointmentAlreadyTakenException();
        }

    }

    @Cacheable(value = "availability", key = "#request.companyNickname + '-' + #request.date")
    public List<AppointmentDto> allAvailableAppointments(AppointmentRequest request) {
        String employeeName = (request.name() == null) ? "" : request.name().toLowerCase();
        String companyNickname = request.companyNickname().toLowerCase();

        Set<Integer> days = operatingHoursService.workDays(companyNickname);
        if (!days.contains(request.date().getDayOfWeek().getValue())) throw new OperatingDayDontExistException();

        List<AppointmentDto> allAvailableHours = new ArrayList<>();
        List<AppointmentsAvailable> allAppointments = appointmentRepository.findAllAppointments(companyNickname, employeeName);

        List<Employees> employees = userService.findAllEmployeesAndIntervalsByCompanyNickname(companyNickname);
        int duration = request.servicesRequest().duration();
        for (Employees employee : employees) {
            LocalTime start = employee.start();
            List<AppointmentsAvailable> list = allAppointments.stream()
                    .filter(appointments -> appointments.name().equals(employee.name()))
                    .toList();

            while (!start.plusMinutes(duration).isAfter(employee.finish())) {
                if (start.isBefore(employee.lunchEnd()) && start.plusMinutes(duration).isAfter(employee.lunchStart())) {
                    start = employee.lunchEnd();
                    continue;
                }
                LocalTime finalStart = start;
                boolean hasAnyMatch = list
                        .stream()
                        .anyMatch(dateTime -> finalStart.isBefore(dateTime.scheduledTime().toLocalTime().plusMinutes(dateTime.duration())) && finalStart.plusMinutes(duration).isAfter(dateTime.scheduledTime().toLocalTime()));

                if (!hasAnyMatch) {
                    allAvailableHours.add(new AppointmentDto(employee.name(), LocalDateTime.of(request.date(), finalStart)));
                }

                start = start.plusMinutes(duration);
            }
        }

        allAvailableHours.removeIf(appointment -> appointment.dateTime().isBefore(LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))));

        return allAvailableHours;
    }

    @Transactional
    public void employeeCancelAppointment(String appointmentPublicId) {
        Appointments appointment = appointmentRepository.findByPublicId(UUID.fromString(appointmentPublicId)).orElseThrow();

        String email = userService.currentUser().getClaimAsString("email");

        if (!(userService.findUserByEmail(email).isPresent() && appointment.getUser().getEmail().equals(email)))
            throw new RuntimeException();

        appointment.setStatus(Status.CANCELED);

        // value referente to the api twillo compared to the credits
        int val = 0;

        Company company = appointment.getCompany();
        if ((company.getPlan().equals(Plan.PRO) && company.getCredits().getDefaultCredits().compareTo(BigDecimal.valueOf(val)) > -1)  || company.getCredits().getBoughtCredits().compareTo(BigDecimal.valueOf(val)) > -1) {
            CompletableFuture.runAsync(() -> {
                // Send notification using credits
            });
        }

    }

    @Transactional
    public void cancelAppointment(String appointmentPublicId, String customerPublicId) {
        Appointments appointments = appointmentRepository.cancelAppointment(appointmentPublicId, customerPublicId).orElseThrow();
        appointments.setStatus(Status.CANCELED);

        String email = appointments.getUser().getEmail();

        if (sseEmitters.containsKey(email)) {
            try {
                sseEmitters.get(email).send("cancelado");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<LocalDate> daysAvailable(LocalDate localDate,String companyNickname, Long serviceId) {
        List<LocalDate> days = new ArrayList<>();

        localDate = localDate.plusDays(1);

        for (int i = 0; i < 3; i++) {
            List<AppointmentDto> appointmentDtos = allAvailableAppointments(new AppointmentRequest(localDate.toString(), companyNickname, "", new ServicesRequest("", serviceId.intValue())));
            if (!appointmentDtos.isEmpty()) days.add(localDate);
            localDate = localDate.plusDays(1);
        }

        return days;
    }

    public void deleteAppointments() {
        appointmentRepository.delete();
    }

    public List<AppointmentsAvailable> appointmentsForToday(String companyNickname) {
        String email = userService.currentUser().getClaimAsString("email");
        User user = userService.findUserByEmail(email).orElseThrow();
        if (!user.getCompany().getNickname().equals(companyNickname)) {
            throw new RuntimeException();
        }
        return appointmentRepository.findAllAppointments(companyNickname, user.getName());
    }
    public List<Appointments> appointmentsForToday(LocalDateTime startDay, LocalDateTime endDay, Long id) {
        return appointmentRepository.findAppointments(startDay, endDay, id);
    }

    public void saveAll(List<Appointments> appointments) {
        appointmentRepository.saveAll(appointments);
    }

    public void addEmitter(SseEmitter sseEmitter) {
        String email = userService.currentUser().getClaimAsString("email");
        sseEmitters.put(email, sseEmitter);
        sseEmitter.onCompletion(() -> sseEmitters.remove(email, sseEmitter));
        sseEmitter.onTimeout(() -> sseEmitters.remove(email, sseEmitter));
    }

    @Transactional
    public void changeDate(String uuid, LocalDateTime date) {
        Appointments appointments = appointmentRepository.findByPublicId(UUID.fromString(uuid)).orElseThrow();

        appointments.setScheduledTime(date);
    }
}
