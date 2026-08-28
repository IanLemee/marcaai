package com.tech.agendaai.company.model.appointment;

import java.time.LocalDateTime;

public record AppointmentsAvailable(String name, String customerName, int duration, LocalDateTime scheduledTime, Status status) {
}
