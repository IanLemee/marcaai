package com.tech.agendaai.company.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.tech.agendaai.company.config.AbacatePayProperties;
import com.tech.agendaai.company.model.company.*;
import com.tech.agendaai.company.model.credits.CompanyCredits;
import com.tech.agendaai.company.model.user.User;
import com.tech.agendaai.company.model.user.UserNotFoundException;
import com.tech.agendaai.company.repository.CompanyRepository;
import com.tech.agendaai.company.service.abacatepay.AbacatePayClientWrapperAPI;
import com.tech.agendaai.company.service.abacatepay.AbacatePayHttpClient;
import com.tech.agendaai.company.service.abacatepay.ClientData;
import com.tech.agendaai.company.service.abacatepay.ClientMetadata;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserService userService;
    private final AbacatePayHttpClient abacatePayHttpClient;

    private final AbacatePayProperties abacatePayProperties;

    private static final String FREE_TIER = "FREE";
    private static final String PREMIUM_TIER = "PREMIUM";
    private static final String PRO_TIER = "PRO";

    public CompanyService(CompanyRepository companyRepository, @Lazy UserService userService, AbacatePayHttpClient abacatePayHttpClient, AbacatePayProperties abacatePayProperties) {
        this.companyRepository = companyRepository;
        this.userService = userService;
        this.abacatePayHttpClient = abacatePayHttpClient;
        this.abacatePayProperties = abacatePayProperties;
    }


    public CreateCompanyResponse create(RegisterCompanyRequest request) {
        Jwt currentUser = userService.currentUser();

        User user = userService.findUserByEmail(currentUser.getClaimAsString("email")).orElseThrow(UserNotFoundException::new);

        String nickname = request.companyDto().nickname().toLowerCase();
        findByNickname(nickname).ifPresent(e -> {
            throw new CompanyAlreadyExistException("Company nickname %s already exist".formatted(request.companyDto().nickname()));
        });

        CreateCompanyRequest createCompanyRequest = request.companyDto();

        Company company = Company
                .builder()
                .name(createCompanyRequest.name().toLowerCase())
                .nickname(createCompanyRequest.nickname().toLowerCase())
                .plan(planFor(createCompanyRequest.plan()))
                .createdAt(Instant.now())
                .publicId(UuidCreator.getTimeOrderedEpoch())
                .credits(new CompanyCredits())
                .build();

        sendToAbacatePay(company);

        user.setCompany(company);

        Company saved = companyRepository.save(company);

        return new CreateCompanyResponse(saved.getPublicId(), saved.getNickname());
    }

    private void sendToAbacatePay(Company company) {
        ClientData data = new ClientData(company.getEmail(), company.getName(), company.getTaxId(), company.getPhoneNumber());
        ClientMetadata metadata = new ClientMetadata(company.getPlan());

        AbacatePayClientWrapperAPI clientWrapperAPI = new AbacatePayClientWrapperAPI(data, metadata);
        abacatePayHttpClient.send(clientWrapperAPI, PayloadCompanyResponse.class, abacatePayProperties.getClient());
    }

    public Optional<Company> findByNickname(String nickname) {
        return companyRepository.findByNickname(nickname.toLowerCase());
    }

    private Plan planFor(String plan) {

        return switch (plan.toUpperCase()) {
            case FREE_TIER -> Plan.FREE;
            case PREMIUM_TIER -> Plan.PREMIUM;
            case PRO_TIER -> Plan.PRO;
            default -> throw new IllegalArgumentException();
        };
    }

    @Transactional
    public void changePlan(String nickname, String plan) {
        Company company = findByNickname(nickname.toLowerCase()).orElseThrow();
        company.setPlan(planFor(plan));
    }

    @Transactional
    public void addCredits(Integer credits, String companyId) {
        Company company = companyRepository.findByPublicId(UUID.fromString(companyId));
        company.getCredits().setBoughtCredits(credits);
    }
}
