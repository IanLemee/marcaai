package com.tech.agendaai.company.model.customer;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record CreateCustomerRequestUser(
        @NotEmpty(message = "Name cannot be empty or null")
        String name,
        String phoneNumber,
        @NotEmpty(message = "Company nickname cannot be empty or null")
        @Min(value = 3, message = "Company nickname should contain at least 3 characters")
        String companyNickname
) {
}
