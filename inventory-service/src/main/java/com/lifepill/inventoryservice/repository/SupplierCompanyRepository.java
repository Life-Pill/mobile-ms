package com.lifepill.inventoryservice.repository;

import com.lifepill.inventoryservice.entity.SupplierCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for SupplierCompany entity
 */
@Repository
public interface SupplierCompanyRepository extends JpaRepository<SupplierCompany, Long> {
    
    Optional<SupplierCompany> findByCompanyEmail(String email);
    
    Optional<SupplierCompany> findByCompanyName(String name);
    
    boolean existsByCompanyEmail(String email);
    
    boolean existsByCompanyName(String name);
}
