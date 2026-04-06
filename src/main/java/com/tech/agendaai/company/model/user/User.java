package com.tech.agendaai.company.model.user;

import com.tech.agendaai.company.model.company.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Table(name = "USERS")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;
    @Column(name = "ROLE", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

}
