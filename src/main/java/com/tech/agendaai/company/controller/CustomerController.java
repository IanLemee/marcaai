package com.tech.agendaai.company.controller;

import com.tech.agendaai.company.model.customer.CreateCustomerRequest;
import com.tech.agendaai.company.service.CustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("create")
    public void create(@RequestBody CreateCustomerRequest request) {
        customerService.createCustomer(request);
    }
}
