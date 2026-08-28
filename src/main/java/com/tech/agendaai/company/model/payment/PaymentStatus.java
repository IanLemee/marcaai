package com.tech.agendaai.company.model.payment;

public enum PaymentStatus {
    PENDING("pending"),
    EXPIRED("expired"),
    CANCELLED("cancelled"),
    PAID("paid"),
    REFUNDED("refunded");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    private String getValue() {
        return this.value;
    }
}
