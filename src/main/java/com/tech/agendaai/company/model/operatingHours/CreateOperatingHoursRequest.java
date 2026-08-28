package com.tech.agendaai.company.model.operatingHours;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record CreateOperatingHoursRequest(
//        @NotEmpty(message = "Day of the week mustn't be null")
        int dayOfWeek,
//        @NotEmpty(message = "Opening hour mustn't be null")
        String openAt,
//        @NotEmpty(message = "Closing hour mustn't be null")
        String closeAt,
//        @NotEmpty(message = "Company nickname cannot be empty or null")
//        @Min(value = 3, message = "Company nickname should contain at least 3 characters")
        String companyNickname
) {
}
