package com.tech.agendaai.company.model.credits;

import com.tech.agendaai.company.model.company.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "COMPANY_CREDITS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyCredits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer defaultCredits;
    private Integer boughtCredits;

    @OneToOne(mappedBy = "CREDITS", cascade = CascadeType.ALL)
    private Company company;
}
