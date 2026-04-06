package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.operatingHours.*;
import com.tech.agendaai.company.model.company.CompanyNotFoundException;
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

    public OperatingHoursService(OperatingHoursRepository operatingHoursRepository, CompanyService companyService) {
        this.operatingHoursRepository = operatingHoursRepository;
        this.companyService = companyService;
    }

    public OpenAndClose getOperatingHoursAt(LocalDate currentDay, String companyNickname) {
        return operatingHoursRepository.findOperatingHoursCompanyAndDay(currentDay.getDayOfWeek().getValue(), companyNickname);
    }

    public Set<Integer> workDays(String nickname) {
        return operatingHoursRepository.workingDays(nickname);
    }


    private WeekDay dayOfWeekFor(int day) {
        final int MONDAY = 1;
        final int TUESDAY = 2;
        final int WEDNESDAY = 3;
        final int THURSDAY = 4;
        final int FRIDAY = 5;
        final int SATURDAY = 6;
        final int SUNDAY = 7;

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

        operatingHoursRepository.saveAll(operatingHours);
    }

    public void changeOperatingHour(ChangeOperatingHour date) {
        int value = date.date().getDayOfWeek().getValue();
        LocalTime open = date.openAndClose().open();
        LocalTime close = date.openAndClose().close();
        operatingHoursRepository.change(value, open, close);
    }
}
