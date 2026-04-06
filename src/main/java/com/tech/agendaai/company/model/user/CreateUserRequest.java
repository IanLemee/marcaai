package com.tech.agendaai.company.model.user;

public record CreateUserRequest(String name, String email, String phone, String role, String companyNickname) {
}
