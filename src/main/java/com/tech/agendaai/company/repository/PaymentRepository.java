package com.tech.agendaai.company.repository;

import com.tech.agendaai.company.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
