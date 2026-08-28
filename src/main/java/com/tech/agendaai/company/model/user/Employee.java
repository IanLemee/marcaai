package com.tech.agendaai.company.model.user;

import com.tech.agendaai.company.model.interval.IntervalsDto;

public record Employee(String name, IntervalsDto intervalsDto) {
}
