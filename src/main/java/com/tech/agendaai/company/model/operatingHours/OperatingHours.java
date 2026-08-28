package com.tech.agendaai.company.model.operatingHours;

import com.tech.agendaai.company.model.company.Company;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Table(name = "OPERATING_HOURS")
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
    @NotNull(message = "Day of Week can not be null")
    @Column(name = "DAY_OF_WEEK", nullable = false)
    private WeekDay dayOfWeek;
    @NotNull(message = "Opening can not be null")
    @Column(name = "OPEN", nullable = false)
    private LocalTime openAt;
    @NotNull(message = "Closing can not be null")
    @Column(name = "CLOSE", nullable = false)
    private LocalTime closeAt;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
