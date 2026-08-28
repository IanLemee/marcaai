package com.tech.agendaai.company.model.payment;

import java.util.List;

public record PayloadRequest(List<PayloadItems> items, String customerId, String externalId, String returnUrl, String completionUrl, List<String> methods, PayloadMetadata metadata) {
}
