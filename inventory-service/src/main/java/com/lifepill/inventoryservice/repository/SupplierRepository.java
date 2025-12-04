package com.lifepill.inventoryservice.repository;

import com.lifepill.inventoryservice.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Supplier entity
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    Optional<Supplier> findBySupplierEmail(String email);
    
    List<Supplier> findByIsActive(boolean isActive);
    
    List<Supplier> findBySupplierCompanyCompanyId(Long companyId);
    
    boolean existsBySupplierEmail(String email);
    
    List<Supplier> findBySupplierNameContainingIgnoreCase(String name);
}
