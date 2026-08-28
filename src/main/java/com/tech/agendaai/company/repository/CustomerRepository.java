package com.tech.agendaai.company.repository;

import com.tech.agendaai.company.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByPublicId(UUID publicId);

    @NativeQuery(value = "delete from customer c using appointments a where c.phone_n is null and a.status in ('DONE', 'CANCELED')")
    void deleteNoNumbers();

    Optional<Customer> findByPhoneNumber(String s);
}
