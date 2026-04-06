package com.tech.agendaai.company.repository;

import com.tech.agendaai.company.model.interval.LunchInterval;
import com.tech.agendaai.company.model.interval.LunchIntervalResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LunchIntervalRepository extends JpaRepository<LunchInterval, Long> {
    @Query(nativeQuery = true, value = "SELECT i.interval_start, i.interval_end from INTERVALS i where i.id = ?1")
    LunchIntervalResponse getInterval(Long intervalId);
}
