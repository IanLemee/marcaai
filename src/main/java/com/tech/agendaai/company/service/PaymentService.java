package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.payment.*;
import com.tech.agendaai.company.repository.PaymentRepository;
import com.tech.agendaai.company.service.abacatepay.AbacatePayHttpClient;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    private final AbacatePayHttpClient payHttpClient;
    private final PaymentRepository paymentRepository;
    private final Map<String, WebhookHandler> webhookHandlerMap = new HashMap<>();

    private static final String PATH = "";

    public PaymentService(AbacatePayHttpClient payHttpClient, PaymentRepository paymentRepository, List<WebhookHandler> webhookHandler) {
        this.payHttpClient = payHttpClient;
        this.paymentRepository = paymentRepository;
        webhookHandler.forEach(handler -> webhookHandlerMap.put(handler.getSupportedEvent(), handler));
    }

    public void processWebhook(PayloadEvent webhookRequest) {
        String[] splitEvent = webhookRequest.event().split("\\.");
        webhookHandlerMap.get(splitEvent[0]).processEvent(splitEvent[1], webhookRequest);
    }

    public String sendCheckout(@Valid PayloadRequest payloadRequest) {
        PayloadResponse response = sendAPIRequest(payloadRequest);
        save(response.data());
        return response.data().url();
    }

    private PayloadResponse sendAPIRequest(PayloadRequest payloadRequest) {
        return payHttpClient.send(payloadRequest, PayloadResponse.class, PATH);
    }

    public void save(PayloadData request) {
        // Add parameter company public ID, so I can know which company it belongs
        // IF this breaks somehow, RollBack
         Payment payment = Payment.builder()
                .createdAt(request.createdAt())
                .updatedAt(request.updatedAt())
                .publicId(request.id())
                .paymentStatus(request.status())
                .serviceType()
                .amount(request.amount())
                .source()
                .customer(request.customerId())
                .service(request.payloadItems().id())
                .quantity(request.payloadItems().quantity())
                .company(request.metadata().companyId())
                .build();
        paymentRepository.save(payment);
    }
}
