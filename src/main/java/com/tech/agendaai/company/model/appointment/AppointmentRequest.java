package com.tech.agendaai.company.model.appointment;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AppointmentRequest(
        @NotNull
        LocalDate date,
        @NotNull
        String nickname,
        String name,
        ServicesRequest servicesRequest
        ) {
}
