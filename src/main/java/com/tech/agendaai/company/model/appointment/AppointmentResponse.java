package com.tech.agendaai.company.model.appointment;

import java.time.LocalDateTime;

public record AppointmentResponse(String name, int duration, LocalDateTime scheduledTime) {
}
