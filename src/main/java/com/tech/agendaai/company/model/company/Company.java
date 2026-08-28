package com.tech.agendaai.company.model.company;

import com.tech.agendaai.company.model.credits.CompanyCredits;
import com.tech.agendaai.company.model.operatingHours.OperatingHours;
import com.tech.agendaai.company.model.services.Services;
import com.tech.agendaai.company.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @NotNull(message = "Name can not be null")
    @Column(name = "NAME", nullable = false)
    private String name;
    @NotNull(message = "Nickname can not be null")
    @Column(name = "NICKNAME",nullable = false, unique = true)
    private String nickname;
    @NotNull(message = "Plan can not be null")
    @Column(name = "PLAN",nullable = false)
    @Enumerated(EnumType.STRING)
    private Plan plan;
    @NotNull(message = "Created at can not be null")
    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;
    @NotNull(message = "Public ID can not be null")
    @Column(name = "PUBLIC_ID", nullable = false)
    private UUID publicId;
    @NotNull(message = "Tax ID can not be null")
    @Column(name = "TAX_ID", nullable = false)
    private String taxId;
    @NotNull(message = "Phone Number can not be null")
    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;
    @NotNull(message = "E-mail can not be null")
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @OneToOne
    @JoinColumn(name = "credits_id")
    private CompanyCredits credits;
    @OneToOne
    @JoinColumn(name = "company_photo_id")
    private CompanyPhoto photo;
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
