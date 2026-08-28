package com.tech.agendaai.company.model.customer;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateCustomer(
        String name,
        String phoneNumber,
        String companyNickname
) {
}
