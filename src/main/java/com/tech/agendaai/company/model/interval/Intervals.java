package com.tech.agendaai.company.model.interval;

import com.tech.agendaai.company.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Intervals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start can not be null")
    @Column(name = "START", nullable = false)
    private LocalTime start;
    @NotNull(message = "Finish can not be null")
    @Column(name = "FINISH", nullable = false)
    private LocalTime finish;
    @NotNull(message = "Lunch start can not be null")
    @Column(name = "LUNCH_START", nullable = false)
    private LocalTime lunchStart;
    @NotNull(message = "Lunch end can not be null")
    @Column(name = "LUNCH_END", nullable = false)
    private LocalTime lunchEnd;

    @OneToOne(mappedBy = "intervals")
    private User user;
}
