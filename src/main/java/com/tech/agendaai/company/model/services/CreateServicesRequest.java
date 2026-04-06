package com.tech.agendaai.company.model.services;

import java.math.BigDecimal;

public record CreateServicesRequest(
        String name,
        String description,
        BigDecimal price,
        int duration,
        String companyNickname
) {}
