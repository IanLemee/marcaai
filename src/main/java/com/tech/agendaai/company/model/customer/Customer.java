package com.tech.agendaai.company.model.customer;

import com.tech.agendaai.company.model.company.Company;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

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

    @NotNull(message = "Public ID can not be null")
    @Column(name = "PUBLIC_ID", nullable = false)
    private String publicId;

    @NotNull(message = "Name can not be null")
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "PHONE_N",unique = true)
    private String phoneNumber;
    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;
    @Column(name = "TAX_ID", unique = true)
    private String taxId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
