package com.tech.agendaai.company.model.appointment;

import com.tech.agendaai.company.exception.BaseAppException;

public class AppointmentAlreadyTakenException extends BaseAppException {
    public AppointmentAlreadyTakenException() {
        super("Appointment already taken");
    }
    public AppointmentAlreadyTakenException(String message) {
        super(message);
    }
}
