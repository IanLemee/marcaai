package com.tech.agendaai.company.repository;

import com.tech.agendaai.company.model.operatingHours.OpenAndClose;
import com.tech.agendaai.company.model.operatingHours.OperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.Set;

public interface OperatingHoursRepository extends JpaRepository<OperatingHours, Long> {

    @Query(value = "select op.open, op.close from operating_hours op join public.company c on op.company_id = c.id where op.day_of_week = ?1 and c.nickname = ?2", nativeQuery = true)
    OpenAndClose findOperatingHoursCompanyAndDay(int currentDay, String companyNickname);

    @Query(value = "select op.day_of_week from operating_hours op join company c on c.id = op.company_id where c.nickname = ?1", nativeQuery = true)
    Set<Integer> workingDays(String companyNickname);

    @Modifying
    @Query(value = "UPDATE OperatingHours op set op.openAt = ?2, op.closeAt = ?3 where op.dayOfWeek = ?1")
    void change(int value, LocalTime open, LocalTime close);
}
