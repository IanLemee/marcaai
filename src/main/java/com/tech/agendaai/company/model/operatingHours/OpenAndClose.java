package com.tech.agendaai.company.model.operatingHours;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalTime;

public record OpenAndClose(
        @NotEmpty(message = "Opening hour mustn't be null")
        LocalTime open,
        @NotEmpty(message = "Closing hour mustn't be null")
        LocalTime close) {
}
