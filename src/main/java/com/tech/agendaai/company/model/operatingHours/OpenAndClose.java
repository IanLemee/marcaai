package com.tech.agendaai.company.model.operatingHours;

import java.time.LocalTime;

public record OpenAndClose(LocalTime open, LocalTime close, Long interval_id) {
}
