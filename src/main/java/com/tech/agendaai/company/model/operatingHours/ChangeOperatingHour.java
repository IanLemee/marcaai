package com.tech.agendaai.company.model.operatingHours;

import java.time.LocalDate;

public record ChangeOperatingHour(LocalDate date, OpenAndClose openAndClose) {
}
