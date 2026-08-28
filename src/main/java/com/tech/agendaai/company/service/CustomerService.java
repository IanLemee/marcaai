package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.company.Company;
import com.tech.agendaai.company.model.company.CompanyNotFoundException;
import com.tech.agendaai.company.model.customer.*;
import com.tech.agendaai.company.repository.CustomerRepository;
import com.tech.agendaai.company.service.abacatepay.AbacatePayHttpClient;
import com.tech.agendaai.company.service.abacatepay.dto.CustomerRequestAbacatePay;
import com.tech.agendaai.company.service.abacatepay.dto.CustomerRequestMetadata;
import com.tech.agendaai.company.service.abacatepay.dto.CustomerResponseAbacatePay;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private static final String CUSTOMER_TYPE_CUSTOMER = "customer";
    private static final String CUSTOMER_URL_PATH = "customers/create";


    private final CustomerRepository customerRepository;
    private final CompanyService companyService;
    private final AbacatePayHttpClient abacatePayHttpClient;

    public CustomerService(CustomerRepository customerRepository, CompanyService companyService, AbacatePayHttpClient abacatePayHttpClient) {
        this.customerRepository = customerRepository;
        this.companyService = companyService;
        this.abacatePayHttpClient = abacatePayHttpClient;
    }

    public CreateCustomerResponse createCustomer(CreateCustomer request) {
        String phoneNumber = (request.phoneNumber().isEmpty() || request.phoneNumber().isBlank()) ? null : request.phoneNumber();

        if (findByPublicId(phoneNumber).isPresent()) {
            throw new IllegalArgumentException();
        }

        String nickname = request.companyNickname().toLowerCase();
        String customerName = request.name().toLowerCase();

        Company company = companyService.findByNickname(nickname).orElseThrow(CompanyNotFoundException::new);
        Customer build = Customer.builder().name(customerName).phoneNumber(phoneNumber).company(company).build();

        String customerPublicId = sendToAbacatePay(build);
        build.setPublicId(customerPublicId);

        Customer saved = customerRepository.save(build);

        return new CreateCustomerResponse(saved.getPublicId());
    }

    public String sendToAbacatePay(Customer customer) {
        CustomerRequestMetadata metadata = new CustomerRequestMetadata(CUSTOMER_TYPE_CUSTOMER, customer.getCompany().getPublicId().toString());
        new CustomerRequestAbacatePay(customer.getEmail(), customer.getName(), customer.getTaxId(), customer.getPhoneNumber(), metadata);
        CustomerResponseAbacatePay responseAbacatePay = abacatePayHttpClient.send(customer, CustomerResponseAbacatePay.class, CUSTOMER_URL_PATH);
        return responseAbacatePay.id();
    }

    public Optional<Customer> findByPublicId(String uuid) {
        return customerRepository.findByPublicId(UUID.fromString(uuid));
    }

    public void deleteNullNumbers() {
        customerRepository.deleteNoNumbers();
    }

    // Double Check this
    public UUID loginCustomer(CustomerLoginRequest request) {
        Customer customer = customerRepository.findByPhoneNumber(request.phoneNumber()).orElseThrow();
        if (!customer.getName().equals(request.name().toLowerCase())) {
            throw new IllegalArgumentException();
        }

        return customer.getPublicId();
    }

    @Transactional
    public void updateCustomerName(String publicId, String name) {
        Customer customer = findByPublicId(publicId).orElseThrow();
        if (customer.getName().equals(name.toLowerCase())) return;
        customer.setName(name.toLowerCase());
    }

}
