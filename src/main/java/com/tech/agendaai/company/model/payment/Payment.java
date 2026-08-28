package com.tech.agendaai.company.model.payment;

import com.tech.agendaai.company.model.company.Company;
import com.tech.agendaai.company.model.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "PAYMENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "PAYMENT_ID", unique = true,nullable = false)
    private String paymentId;
    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;
    @Column(name = "UPDATED_AT", nullable = false)
    private Instant updatedAt;
    @Column(name = "PAYMENT_EVENT_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentEventType paymentEventType;
    @Column(name = "SERVICE_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
    //There's no payment method data
//    @Column(name = "PAYMENT_METHOD", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private PaymentMethod paymentMethod;
    @Column(name = "PAYMENT_STATUS", nullable = false)
    private PaymentStatus paymentStatus;
    @Column(name = "AMOUNT", nullable = false)
    private Long amount;
    @Column(name = "source")
    private String source;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

}
