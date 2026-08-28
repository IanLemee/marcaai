package com.tech.agendaai.company.model.payment;

public enum PaymentEventType {
    CHECKOUT_COMPLETE("checkout.completed"),
    CHECKOUT_REFUNDED("checkout.refunded"),
    SUBSCRIPTION_COMPLETED("subscription.completed"),
    SUBSCRIPTION_CANCELLED("subscription.cancelled"),
    SUBSCRIPTION_RENEWED("subscription.renewed");

    private final String value;

    PaymentEventType(String value) {
        this.value = value;
    }

    private String getValue() {
        return this.value;
    }
}
