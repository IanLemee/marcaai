package com.tech.agendaai.company.repository;

import com.tech.agendaai.company.model.services.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicesRepository extends JpaRepository<Services, Long> {
    Optional<Services> findByName(String name);
}
