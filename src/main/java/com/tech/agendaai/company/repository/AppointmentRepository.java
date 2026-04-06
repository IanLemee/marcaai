package com.tech.agendaai.company.repository;

import com.tech.agendaai.company.model.appointment.AppointmentResponse;
import com.tech.agendaai.company.model.appointment.Appointments;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepository<T> extends JpaRepository<Appointments, Long> {
    @Query(nativeQuery = true, value = "SELECT * from get_appointment(?1, ?2)")
    List<AppointmentResponse> findAllAppointments(String nickname, String name);

    @Query(nativeQuery = true, value = "SELECT * from appointments")
    List<Appointments> findAppointments();

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM APPOINTMENTS where status in ('DONE', 'CANCELED')")
    void delete();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    <S extends Appointments> S save(@NonNull S entity);
}
