package com.tech.agendaai.company.model.appointment;

import com.tech.agendaai.company.model.company.Company;
import com.tech.agendaai.company.model.customer.Customer;
import com.tech.agendaai.company.model.services.Services;
import com.tech.agendaai.company.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "APPOINTMENTS")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SCHEDULED", nullable = false)
    private LocalDateTime scheduledTime;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "SERVICE_ID")
    private Services services;
}
