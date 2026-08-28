package com.tech.agendaai.company.model.payment;

public enum ServiceType {
    PRODUCT("product"),
    SERVICE("service"),
    SUBSCRIPTION("subscription");

    private final String value;

    ServiceType(String value) {
        this.value = value;
    }

    private String getValue() {
        return this.value;
    }
}
