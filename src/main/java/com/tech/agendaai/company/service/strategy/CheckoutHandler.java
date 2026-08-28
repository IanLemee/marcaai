package com.tech.agendaai.company.service.strategy;

import com.tech.agendaai.company.model.payment.PayloadEvent;
import com.tech.agendaai.company.model.payment.WebhookHandler;
import com.tech.agendaai.company.model.products.ProductCatalog;
import com.tech.agendaai.company.service.CompanyService;
import com.tech.agendaai.company.service.PaymentService;
import com.tech.agendaai.company.service.ProductCatalogService;
import com.tech.agendaai.company.service.appointment.AppointmentService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CheckoutHandler implements WebhookHandler {
    private static final String CREDITS = "credits";
    private static final String SERVICE = "service";

    private static final String COMPLETED = "completed";
    private static final String REFUNDED = "refunded";

    private final CompanyService companyService;
    private final AppointmentService appointmentService;
    private final ProductCatalogService productCatalogService;
    private final PaymentService paymentService;

    public CheckoutHandler(CompanyService companyService, AppointmentService appointmentService, ProductCatalogService productCatalogService, PaymentService paymentService) {
        this.companyService = companyService;
        this.appointmentService = appointmentService;
        this.productCatalogService = productCatalogService;
        this.paymentService = paymentService;
    }

    @Override
    public String getSupportedEvent() {
        return "checkout";
    }

    @Override
    public void processEvent(String event, PayloadEvent payloadEvent) {
        String purchaseType = payloadEvent.data().metadata().purchaseType();
        if (event.equalsIgnoreCase(COMPLETED)) {
            if (purchaseType.equalsIgnoreCase(CREDITS)) {
                processCredits(payloadEvent);
            } else if (purchaseType.equalsIgnoreCase(SERVICE)) {
                processService(payloadEvent);
            } else {
                throw new IllegalArgumentException();
            }
        }

    }

    private void processService(PayloadEvent event) {
        paymentService.save(event.data());
        appointmentService.confirmAppointment(event);
    }

    private void processCredits(PayloadEvent event) {
        if (!event.data().status().equalsIgnoreCase("PAID")) {
            // TODO DONT return, check the status, if refund, remove credits, LOGIC UNDER THE HOOD AT PAYMENT TO FIND THIS ID, SEE THE AMOUNT AND SO ON
            return;
        } else {
            ProductCatalog product = productCatalogService.findByPublicId(UUID.fromString(event.data().externalId()));
            companyService.addCredits(product.getCredits(), event.data().metadata().companyId());
        }

        paymentService.save(event.data());
    }
}
