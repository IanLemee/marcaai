package com.tech.agendaai.company.model.appointment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record AppointmentRequest(
        LocalDate date,
        String companyNickname,
        String name,
        ServicesRequest servicesRequest
) {

    public AppointmentRequest(String date, String companyNickname, String name, ServicesRequest servicesRequest) {
        this(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")), companyNickname, name, servicesRequest);
    }

}
