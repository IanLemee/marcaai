package com.tech.agendaai.company.model.services;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateServicesRequest(
//        @NotEmpty(message = "Name cannot be empty or null")
//        @Min(value = 3, message = "Name should contain at least 3 characters")
        String name,
//        @Size(min = 10, max = 200)
        String description,
//        @NotEmpty(message = "Price cannot be empty or null")
        BigDecimal price,
//        @NotEmpty(message = "Duration cannot be empty or null")
        int duration,
//        @NotEmpty(message = "Company nickname cannot be empty or null")
//        @Min(value = 3, message = "Company nickname should contain at least 3 characters")
        String companyNickname
) {}
