package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.company.Company;
import com.tech.agendaai.company.model.company.CompanyNotFoundException;
import com.tech.agendaai.company.model.company.Plan;
import com.tech.agendaai.company.model.services.CreateServicesRequest;
import com.tech.agendaai.company.model.services.Services;
import com.tech.agendaai.company.model.user.CreateUserRequest;
import com.tech.agendaai.company.repository.ServicesRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicesServiceTest {
    @Mock
    private ServicesRepository servicesRepository;
    @Mock
    private CompanyService companyService;

    @InjectMocks
    private ServicesService servicesService;

    @Nested
    class createService {
        @Test
        void shouldCreateServiceWhenSuccessful() {
            var company = Company.builder()
                    .name("Company")
                    .nickname("company1")
                    .plan(Plan.FREE)
                    .createdAt(Instant.now())
                    .build();
            var request = new CreateServicesRequest("Hair cut", "", BigDecimal.valueOf(40), 60, "company1");
            var hairCut = new Services(null, request.name(), request.description(), request.price(), request.duration(), company);
            var listOfExpectedServices = List.of(hairCut);


            when(servicesRepository.saveAll(anyList())).thenReturn(listOfExpectedServices);
            when(companyService.findByNickname(anyString())).thenReturn(Optional.of(company));
            servicesService.createService(Collections.singletonList(request));

            verify(servicesRepository, times(1)).saveAll(listOfExpectedServices);
            verify(companyService, times(1)).findByNickname(request.companyNickname());
        }

        @Test
        void createUserShouldThrowCompanyNotFoundExceptionWhenCompanyNicknameNotFound(){
            var request =
                    new CreateServicesRequest("Haircut", "", BigDecimal.TEN, 60, "company1");

            when(companyService.findByNickname(anyString())).thenReturn(Optional.empty());

            CompanyNotFoundException companyNotFoundException = assertThrows(CompanyNotFoundException.class, () -> servicesService.createService(List.of(request)));
            String message = companyNotFoundException.getMessage();
            String expectedMessage = "Company not found by nickname";
            assertEquals(expectedMessage, message);
        }
    }
}