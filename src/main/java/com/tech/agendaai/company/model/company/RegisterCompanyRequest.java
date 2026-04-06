package com.tech.agendaai.company.model.company;

import com.tech.agendaai.company.model.user.CreateUserRequest;

public record RegisterCompanyRequest(CreateCompanyRequest companyDto, CreateUserRequest userDto) {
}
