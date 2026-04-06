package com.tech.agendaai.company.dto;

import java.math.BigDecimal;

public record ServiceDto(String name, BigDecimal price, int duration) {
}
