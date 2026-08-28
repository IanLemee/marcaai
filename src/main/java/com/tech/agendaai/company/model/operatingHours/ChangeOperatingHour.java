package com.tech.agendaai.company.model.operatingHours;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public record ChangeOperatingHour(
        @NotEmpty(message = "Date cannot be empty or null")
        LocalDate date,
        OpenAndClose openAndClose
) {
}
