package com.tech.agendaai.company.model.company;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record CreateCompanyRequest(
        @NotEmpty(message = "Name cannot be empty or null")
        @Min(value = 3, message = "Name should contain at least 3 characters")
        String name,
        @NotEmpty(message = "Nickname cannot be empty or null")
        @Min(value = 3, message = "Nickname should contain at least 3 characters")
        String nickname,
        @NotEmpty(message = "You must chose a plan")
        String plan
) {
}
