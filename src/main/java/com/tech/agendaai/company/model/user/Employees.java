package com.tech.agendaai.company.model.user;

import java.time.LocalTime;

public record Employees(String name, LocalTime start, LocalTime finish, LocalTime lunchStart,LocalTime lunchEnd) {
}
