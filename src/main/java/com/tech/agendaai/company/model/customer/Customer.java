package com.tech.agendaai.company.model.customer;

import com.tech.agendaai.company.model.company.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "CUSTOMER", indexes = @Index(name = "idx_number", columnList = "PHONE_N"))
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "PHONE_N",unique = true, nullable = false)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
