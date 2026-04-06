package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.company.CompanyNotFoundException;
import com.tech.agendaai.company.model.services.CreateServicesRequest;
import com.tech.agendaai.company.model.services.Services;
import com.tech.agendaai.company.repository.ServicesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicesService {

    private final ServicesRepository servicesRepository;
    private final CompanyService companyService;

    public ServicesService(ServicesRepository servicesRepository, CompanyService companyService) {
        this.servicesRepository = servicesRepository;
        this.companyService = companyService;
    }

    public void createService(List<CreateServicesRequest> request) {
        List<Services> services = request.stream().map(req ->
                Services
                        .builder()
                        .name(req.name())
                        .description(req.description())
                        .price(req.price())
                        .duration(req.duration())
                        .company(companyService.findByNickname(req.companyNickname()).orElseThrow(CompanyNotFoundException::new))
                        .build()
        ).toList();

        servicesRepository.saveAll(services);
    }

    public Optional<Services> findServiceBy(String name) {
        return servicesRepository.findByName(name);
    }
}
