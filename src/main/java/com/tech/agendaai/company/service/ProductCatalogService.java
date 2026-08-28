package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.products.ProductCatalog;
import com.tech.agendaai.company.repository.ProductCatalogRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductCatalogService {
    private final ProductCatalogRepository catalogRepository;

    public ProductCatalogService(ProductCatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public ProductCatalog findByPublicId(UUID publicId) {
        return catalogRepository.findByPublicId(publicId);
    }
}
