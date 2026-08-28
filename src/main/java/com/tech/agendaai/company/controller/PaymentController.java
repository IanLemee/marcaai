package com.tech.agendaai.company.controller;

import com.tech.agendaai.company.model.payment.PayloadRequest;
import com.tech.agendaai.company.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping()
    public ResponseEntity<String> createCheckout(@Valid @RequestBody PayloadRequest payloadRequest) {
        String response = paymentService.sendCheckout(payloadRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
