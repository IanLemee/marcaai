package com.tech.agendaai.company.model.services;

import com.tech.agendaai.company.model.company.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;
    @Column(name = "DURATION", nullable = false)
    private int duration;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
