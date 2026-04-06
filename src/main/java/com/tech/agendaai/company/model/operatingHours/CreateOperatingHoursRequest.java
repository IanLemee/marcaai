package com.tech.agendaai.company.model.operatingHours;

import jakarta.validation.constraints.NotEmpty;

public record CreateOperatingHoursRequest(
        @NotEmpty
        int dayOfWeek,
        @NotEmpty
        String openAt,
        @NotEmpty
        String closeAt,
        String companyNickname) {
}
