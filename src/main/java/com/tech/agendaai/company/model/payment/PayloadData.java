package com.tech.agendaai.company.model.payment;

import java.math.BigDecimal;
import java.time.Instant;

public record PayloadData(String id, String externalId, String url, BigDecimal amount, PayloadItems payloadItems, String status, String customerId, Instant createdAt, Instant updatedAt, PayloadMetadata metadata) {
}
