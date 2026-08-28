package com.tech.agendaai.company.model.interval;

import java.time.LocalTime;

public record IntervalsDto(LocalTime start, LocalTime finish, LocalTime lunchStart, LocalTime lunchEnd) {
}
