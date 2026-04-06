package com.tech.agendaai.company.model.company;

import com.tech.agendaai.company.model.operatingHours.OperatingHours;
import com.tech.agendaai.company.model.services.Services;
import com.tech.agendaai.company.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Table(name = "COMPANY", indexes = @Index(name = "idx_nickname", columnList = "NICKNAME", unique = true))
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "NICKNAME",nullable = false, unique = true)
    private String nickname;
    @Column(name = "PLAN",nullable = false)
    @Enumerated(EnumType.STRING)
    private Plan plan;
    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<User> users = new ArrayList<>();
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OperatingHours> operatingHours = new ArrayList<>();
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Services> services = new ArrayList<>();

    public void syncRelationsFor(User user) {
        users.add(user);
        user.setCompany(this);

    }
}
