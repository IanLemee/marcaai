package com.tech.agendaai.company.model.user;

import java.util.UUID;

public record UserResponse(String name, Role role, UUID publicId) {

}
