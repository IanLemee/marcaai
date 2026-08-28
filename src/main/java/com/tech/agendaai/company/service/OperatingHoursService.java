package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.company.CompanyNotFoundException;
import com.tech.agendaai.company.model.operatingHours.*;
import com.tech.agendaai.company.repository.OperatingHoursRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
public class OperatingHoursService {
    private final OperatingHoursRepository operatingHoursRepository;
    private final CompanyService companyService;

    private static final int MONDAY = 1;
    private static final int TUESDAY = 2;
    private static final int WEDNESDAY = 3;
    private static final int THURSDAY = 4;
    private static final int FRIDAY = 5;
    private static final int SATURDAY = 6;
    private static final int SUNDAY = 7;

    public OperatingHoursService(OperatingHoursRepository operatingHoursRepository, CompanyService companyService) {
        this.operatingHoursRepository = operatingHoursRepository;
        this.companyService = companyService;
    }

    public OpenAndClose getOperatingHoursAt(LocalDate currentDay, String companyNickname) {
        return operatingHoursRepository.findOperatingHoursCompanyAndDay(currentDay.getDayOfWeek().getValue(), companyNickname);
    }

    public Set<Integer> workDays(String companyNickname) {
        return operatingHoursRepository.workingDays(companyNickname);
    }


    private WeekDay dayOfWeekFor(int day) {


        return switch (day) {
            case MONDAY -> WeekDay.MONDAY;
            case TUESDAY -> WeekDay.TUESDAY;
            case WEDNESDAY -> WeekDay.WEDNESDAY;
            case THURSDAY -> WeekDay.THURSDAY;
            case FRIDAY -> WeekDay.FRIDAY;
            case SATURDAY -> WeekDay.SATURDAY;
            case SUNDAY -> WeekDay.SUNDAY;
            default -> throw new IllegalArgumentException();
        };
    }

    public void create(List<CreateOperatingHoursRequest> request) {
        List<OperatingHours> operatingHours = request.stream().map(req ->
                OperatingHours.builder()
                        .dayOfWeek(dayOfWeekFor(req.dayOfWeek()))
                        .openAt(LocalTime.parse(req.openAt()))
                        .closeAt(LocalTime.parse(req.closeAt()))
                        .company(companyService.findByNickname(req.companyNickname()).orElseThrow(CompanyNotFoundException::new))
                        .build()
        ).toList();

        List<OperatingHours> operatingHours1 = operatingHoursRepository.saveAll(operatingHours);
        System.out.println(operatingHours1);
    }

    public void changeOperatingHour(ChangeOperatingHour date) {
        int value = date.date().getDayOfWeek().getValue();
        LocalTime open = date.openAndClose().open();
        LocalTime close = date.openAndClose().close();
        operatingHoursRepository.change(value, open, close);
    }
}
