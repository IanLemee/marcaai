package com.tech.agendaai.company.repository;

import com.tech.agendaai.company.model.credits.CompanyCredits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;
import java.util.Optional;

public interface CompanyCreditsRepository extends JpaRepository<CompanyCredits, Long> {

    @NativeQuery(value = "SELECT cc from companyCredits cc join company c on cc.id = c.credits_id where c.plan = 'PREMIUM' and cc.id > ?1 ORDER BY cc.id LIMIT 100")
    Optional<List<CompanyCredits>> findNextPage(Long id);
}
