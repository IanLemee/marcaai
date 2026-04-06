package com.tech.agendaai.company.model.operatingHours;

import jakarta.persistence.EnumeratedValue;
import lombok.Getter;

@Getter
public enum WeekDay {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    @EnumeratedValue
    final int value;

    WeekDay(int value) {
        this.value = value;
    }
}
