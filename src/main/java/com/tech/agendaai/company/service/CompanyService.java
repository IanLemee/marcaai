package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.company.*;
import com.tech.agendaai.company.model.user.User;
import com.tech.agendaai.company.repository.CompanyRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserService userService;

    public CompanyService(CompanyRepository companyRepository, @Lazy UserService userService) {
        this.companyRepository = companyRepository;
        this.userService = userService;
    }

    public void create(RegisterCompanyRequest request) {
        findByNickname(request.companyDto().nickname()).ifPresent(e -> {
            throw new CompanyAlreadyExistException("Company nickname %s already exist".formatted(request.companyDto().nickname()));
        });

        CreateCompanyRequest createCompanyRequest = request.companyDto();

        Company company = Company
                .builder()
                .name(createCompanyRequest.name())
                .nickname(createCompanyRequest.nickname())
                .plan(planFor(createCompanyRequest.plan()))
                .createdAt(Instant.now())
                .build();

        User user = userService.buildUserFor(request.userDto());
        company.syncRelationsFor(user);

        companyRepository.save(company);
    }

    public Optional<Company> findByNickname(String nickname) {
        return companyRepository.findByNickname(nickname);
    }

    private Plan planFor(String plan) {
        final String FREE_TIER = "FREE";
        final String PREMIUM_TIER = "PREMIUM";
        final String PRO_TIER = "PRO";

        return switch (plan.toUpperCase()) {
            case FREE_TIER -> Plan.FREE;
            case PREMIUM_TIER -> Plan.PREMIUM;
            case PRO_TIER -> Plan.PRO;
            default -> throw new IllegalArgumentException();
        };
    }
}
