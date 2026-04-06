package com.tech.agendaai.company.model.operatingHours;

import com.tech.agendaai.company.model.interval.LunchInterval;
import com.tech.agendaai.company.model.company.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Table(name = "OPERATINGHOURS")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DAY_OF_WEEK", nullable = false)
    private WeekDay dayOfWeek;
    @Column(name = "OPEN", nullable = false)
    private LocalTime openAt;
    @Column(name = "CLOSE", nullable = false)
    private LocalTime closeAt;

    @OneToOne
    @JoinColumn(name = "interval_id")
    private LunchInterval interval;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
