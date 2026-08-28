package com.tech.agendaai.company.model.appointment;

import java.time.LocalDateTime;

public record CreateAppointment(LocalDateTime date, String employeeId, String customerPhoneN, int serviceId) {
}
