package com.tech.agendaai.company.model.payment;

public interface WebhookHandler{
    String getSupportedEvent();
    void processEvent(String event, PayloadEvent data);
}
