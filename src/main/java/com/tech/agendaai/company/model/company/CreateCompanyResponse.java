package com.tech.agendaai.company.model.company;

import java.util.UUID;

public record CreateCompanyResponse(UUID publicId, String nickname) {
}
