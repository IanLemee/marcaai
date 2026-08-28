package com.tech.agendaai.company.controller;

import com.tech.agendaai.company.model.customer.CreateCustomer;
import com.tech.agendaai.company.model.customer.CreateCustomerRequest;
import com.tech.agendaai.company.model.customer.CreateCustomerRequestUser;
import com.tech.agendaai.company.model.customer.CustomerLoginRequest;
import com.tech.agendaai.company.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("customer/create")
    public void create(@RequestBody @Valid CreateCustomerRequest request) {
        CreateCustomer customer = new CreateCustomer(request.name(), request.phoneNumber(), request.companyNickname());
        customerService.createCustomer(customer);
    }

    // TODO rename this method
    @PostMapping("employee/create")
    public void userCreate(@RequestBody @Valid CreateCustomerRequestUser request) {
        CreateCustomer customer = new CreateCustomer(request.name(), request.phoneNumber(), request.companyNickname());
        customerService.createCustomer(customer);
    }

    @PostMapping("/login")
    public UUID customerLogin(@RequestBody CustomerLoginRequest request) {
        return customerService.loginCustomer(request);
    }
}
