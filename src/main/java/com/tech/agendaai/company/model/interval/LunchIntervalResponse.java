package com.tech.agendaai.company.model.interval;

import java.time.LocalTime;

public record LunchIntervalResponse(LocalTime start, LocalTime end) {
}
