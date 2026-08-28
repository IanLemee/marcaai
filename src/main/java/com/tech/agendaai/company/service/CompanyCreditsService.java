package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.credits.CompanyCredits;
import com.tech.agendaai.company.repository.CompanyCreditsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyCreditsService {

    private final CompanyCreditsRepository companyCreditsRepository;

    public CompanyCreditsService(CompanyCreditsRepository companyCreditsRepository) {
        this.companyCreditsRepository = companyCreditsRepository;
    }

    public Optional<List<CompanyCredits>> getNextCompanies(Long id) {
        return companyCreditsRepository.findNextPage(id);
    }

    public void buyCredits() {

    }
}
