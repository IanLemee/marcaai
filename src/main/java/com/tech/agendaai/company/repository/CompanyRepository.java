package com.tech.agendaai.company.repository;

import com.tech.agendaai.company.model.company.Company;
import com.tech.agendaai.company.model.operatingHours.OperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByNickname(String nickname);

//    @Query(value = "select b.scheduled_duration, b.scheduled_time from company_tb a join schedules_tb b on a.id = b.company_id join customer_tb c on a.id = c.company_id", nativeQuery = true)

    @Query(value = "select o.id, o.company_id, o.close_at, o.open_at, o.day_of_week from company_tb c join operatinghours_tb o on c.id = o.company_id", nativeQuery = true)
    List<OperatingHours> findCompanyByNicknameAndGetOperatingHours(String companyNickname);

}
