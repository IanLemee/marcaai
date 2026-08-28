package com.tech.agendaai.company.model.services;

import com.tech.agendaai.company.model.company.Company;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "SERVICES")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @NotNull(message = "Name can not be null")
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @NotNull(message = "Price can not be null")
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;
    @NotNull(message = "Duration can not be null")
    @Column(name = "DURATION", nullable = false)
    private int duration;
    @NotNull(message = "Public ID can not be null")
    @Column(name = "PUBLIC_ID", nullable = false)
    private UUID publicId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
