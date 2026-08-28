package com.tech.agendaai.company.model.interval;

import java.time.LocalTime;

public record IntervalCreateRequest(LocalTime start, LocalTime end, LocalTime lunchStart, LocalTime lunchEnd) {
}
