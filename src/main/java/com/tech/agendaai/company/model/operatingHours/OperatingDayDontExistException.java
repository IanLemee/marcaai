package com.tech.agendaai.company.model.operatingHours;

import com.tech.agendaai.company.exception.BaseAppException;

public class OperatingDayDontExistException extends BaseAppException {
    public OperatingDayDontExistException() {
        super("Company doesn't work this day");
    }
    public OperatingDayDontExistException(String message) {
        super(message);
    }
}
