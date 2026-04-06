package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.company.CompanyNotFoundException;
import com.tech.agendaai.company.model.customer.CreateCustomerRequest;
import com.tech.agendaai.company.model.customer.Customer;
import com.tech.agendaai.company.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyService companyService;

    public CustomerService(CustomerRepository customerRepository, CompanyService companyService) {
        this.customerRepository = customerRepository;
        this.companyService = companyService;
    }

    public Customer createCustomer(CreateCustomerRequest request) {
        return findByPhoneNumber(request.phoneNumber())
                .orElseGet(() -> {
                    Customer customer = Customer.builder().name(request.name()).phoneNumber(request.phoneNumber()).company(companyService.findByNickname(request.companyNickname()).orElseThrow(CompanyNotFoundException::new)).build();
                    return customerRepository.save(customer);
                });
    }

    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }
}
