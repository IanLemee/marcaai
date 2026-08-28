package com.tech.agendaai.company.repository;

import com.tech.agendaai.company.model.products.ProductCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductCatalogRepository extends JpaRepository<ProductCatalog, Integer> {
    ProductCatalog findByPublicId(UUID publicId);
}
