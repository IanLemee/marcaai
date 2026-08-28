package com.tech.agendaai.company.service;

import com.tech.agendaai.company.config.AbacatePayProperties;
import com.tech.agendaai.company.model.company.CompanyNotFoundException;
import com.tech.agendaai.company.model.services.CreateServicesRequest;
import com.tech.agendaai.company.model.services.PayloadServicesResponse;
import com.tech.agendaai.company.model.services.Services;
import com.tech.agendaai.company.repository.ServicesRepository;
import com.tech.agendaai.company.service.abacatepay.AbacatePayHttpClient;
import com.tech.agendaai.company.service.abacatepay.ProductsWrapperAbacate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicesService {
    private final ServicesRepository servicesRepository;
    private final CompanyService companyService;
    private final AbacatePayHttpClient abacatePayHttpClient;
    private final AbacatePayProperties abacatePayProperties;

    public ServicesService(ServicesRepository servicesRepository, CompanyService companyService, AbacatePayHttpClient abacatePayHttpClient, AbacatePayProperties abacatePayProperties) {
        this.servicesRepository = servicesRepository;
        this.companyService = companyService;
        this.abacatePayHttpClient = abacatePayHttpClient;
        this.abacatePayProperties = abacatePayProperties;
    }

    public void createService(List<CreateServicesRequest> request) {
        ArrayList<CreateServicesRequest> list = new ArrayList<>();

        // Refactor this, maybe extract to another method, but use streams for sure
        for (CreateServicesRequest req : request) {
            if (findServiceByName(req.name()).isEmpty()) {
                list.add(req);
            }
        }

        List<Services> services = list.stream().map(req ->
                Services
                        .builder()
                        .name(req.name())
                        .description(req.description())
                        .price(req.price())
                        .duration(req.duration())
                        .company(companyService.findByNickname(req.companyNickname()).orElseThrow(CompanyNotFoundException::new))
                        .build()
        ).toList();

        services.forEach(this::sendRequestToAbacate);

        servicesRepository.saveAll(services);
    }

    private void sendRequestToAbacate(Services service) {
        ProductsWrapperAbacate productsWrapperAbacate = new ProductsWrapperAbacate(service.getPublicId().toString(), service.getName(), service.getPrice().multiply(BigDecimal.valueOf(100)).longValueExact(), "BRL");

        abacatePayHttpClient.send(productsWrapperAbacate, PayloadServicesResponse.class, abacatePayProperties.getProduct());
    }

    private Optional<Services> findServiceByName(String name) {
        return servicesRepository.findByName(name);
    }

    public Optional<Services> findServiceById(long id) {
        return servicesRepository.findById(id);
    }
}
