package com.tech.agendaai.company.model.interval;

import com.tech.agendaai.company.model.operatingHours.OperatingHours;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Table(name = "INTERVALS")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LunchInterval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "interval_start")
    private LocalTime intervalStart;
    @Column(name = "interval_end")
    private LocalTime intervalEnd;

    @OneToOne(mappedBy = "interval")
    private OperatingHours operatingHours;
}
