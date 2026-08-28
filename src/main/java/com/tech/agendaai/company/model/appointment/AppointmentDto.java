package com.tech.agendaai.company.model.appointment;

import java.time.LocalDateTime;

public record AppointmentDto(String name,
                             LocalDateTime dateTime) {
}
