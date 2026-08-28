package com.tech.agendaai.company.service.strategy;

import com.tech.agendaai.company.model.payment.PayloadEvent;
import com.tech.agendaai.company.model.payment.WebhookHandler;
import com.tech.agendaai.company.service.CompanyService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionHandler implements WebhookHandler {

    private static final String SUBSCRIPTION_COMPLETED = "completed";
    private static final String SUBSCRIPTION_RENEWED = "renewed";
    private static final String SUBSCRIPTION_CANCELLED = "cancelled";

    private final CompanyService companyService;

    public SubscriptionHandler(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public String getSupportedEvent() {
        return "subscription";
    }

    @Override
    public void processEvent(String event, PayloadEvent data) {
        if (event.equalsIgnoreCase(SUBSCRIPTION_COMPLETED)) {

        } else if (event.equalsIgnoreCase(SUBSCRIPTION_RENEWED)) {

        } else if (event.equalsIgnoreCase(SUBSCRIPTION_CANCELLED)) {

        }
    }


}
