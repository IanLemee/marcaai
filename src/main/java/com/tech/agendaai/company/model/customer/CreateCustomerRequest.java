package com.tech.agendaai.company.model.customer;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(
        @NotEmpty(message = "Name cannot be empty or null")
        String name,
        @NotEmpty(message = "Phone number cannot be empty or null")
        @Size(min = 11, max = 11)
        String phoneNumber,
        @NotEmpty(message = "Company nickname cannot be empty or null")
        @Min(value = 3, message = "Company nickname should contain at least 3 characters")
        String companyNickname
) {
}
