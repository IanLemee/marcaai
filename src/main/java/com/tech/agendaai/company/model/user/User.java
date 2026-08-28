package com.tech.agendaai.company.model.user;

import com.tech.agendaai.company.model.company.Company;
import com.tech.agendaai.company.model.interval.Intervals;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

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

    @NotNull(message = "Name can not be null")
    @Column(name = "NAME", nullable = false)
    private String name;
    @NotNull(message = "E-mail can not be null")
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    @NotNull(message = "Created at can not be null")
    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;
    @NotNull(message = "Role can not be null")
    @Column(name = "ROLE", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @NotNull(message = "Public ID at can not be null")
    @Column(name = "PUBLIC_ID", nullable = false)
    private String publicId;
    @Column(name = "PHONE_NUMBER", unique = true)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @OneToOne
    @JoinColumn(name = "INTERVAL_ID")
    private Intervals intervals;
}
