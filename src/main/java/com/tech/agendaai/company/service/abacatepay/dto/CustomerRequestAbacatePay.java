package com.tech.agendaai.company.service.abacatepay.dto;

public record CustomerRequestAbacatePay(String email, String name, String taxId, String cellphone, CustomerRequestMetadata metadata) {
}
