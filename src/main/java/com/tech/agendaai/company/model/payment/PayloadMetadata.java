package com.tech.agendaai.company.model.payment;
/**
 * Source -> how it came? APP, Website.
 * CompanyId -> publicId of a company
 * PurchaseType -> if it's a subscription or no
 * */
public record PayloadMetadata(String source, String companyId, String purchaseType) {
}
