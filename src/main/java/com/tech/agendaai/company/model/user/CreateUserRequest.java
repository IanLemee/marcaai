package com.tech.agendaai.company.model.user;

import jakarta.validation.constraints.*;

public record CreateUserRequest(
        @NotEmpty(message = "Name cannot be empty or null")
        @Min(value = 3, message = "Name should contain at least 3 characters")
        String name,
        @NotEmpty(message = "Email cannot be empty or null")
        @Email(
                message = "Email should be valid",
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
        )
        String email,
        @NotEmpty(message = "Phone number cannot be empty or null")
        @Size(min = 11, max = 11, message = "Phone number should contain at least 11 characters")
        String phone,
        @NotEmpty(message = "Role cannot be empty or null")
        String role,
        @NotEmpty(message = "Company nickname cannot be empty or null")
        @Min(value = 3, message = "Company nickname should contain at least 3 characters")
        String companyNickname
) {
}
