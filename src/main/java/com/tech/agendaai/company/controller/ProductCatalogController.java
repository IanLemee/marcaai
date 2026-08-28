package com.tech.agendaai.company.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product-catalog")
public class ProductCatalogController {

    @PostMapping("create-product")
    public ResponseEntity<Void> createProduct() {
        return null;
    }
}
