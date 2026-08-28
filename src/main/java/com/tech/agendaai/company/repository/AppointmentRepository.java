package com.tech.agendaai.company.repository;

import com.tech.agendaai.company.model.appointment.AppointmentsAvailable;
import com.tech.agendaai.company.model.appointment.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointments, Long> {
    @Query(nativeQuery = true, value = "SELECT * from get_appointment(?1, ?2)")
    List<AppointmentsAvailable> findAllAppointments(String companyId, String name);

    @Query(nativeQuery = true, value = "SELECT * from appointments a where a.id > ?3 and a.status = 'CONFIRMED' and a.scheduled >= ?1 and a.scheduled <= ?2 ORDER BY a.id limit 100")
    List<Appointments> findAppointments(LocalDateTime startDay, LocalDateTime endDay, Long id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM APPOINTMENTS a using customer c where a.status in ('DONE', 'CANCELED')")
    void delete();

    @Query(nativeQuery = true, value = "SELECT * from appointments ap join customer c on ap.customer_id = c.id where ap.public_id = ?1 and c.public_id = ?2")
    Optional<Appointments> cancelAppointment(String publicAppointmentId, String publicCustomerId);

    Optional<Appointments> findByPublicId(UUID publicId);
}
