package com.tech.agendaai.company.model.payment;

public record PayloadEvent(String id, String event, PayloadData data) {
}
