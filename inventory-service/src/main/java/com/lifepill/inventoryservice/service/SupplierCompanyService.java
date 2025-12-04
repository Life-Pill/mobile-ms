package com.lifepill.inventoryservice.service;

import com.lifepill.inventoryservice.dto.SupplierCompanyDTO;

import java.util.List;

/**
 * Service interface for SupplierCompany operations
 */
public interface SupplierCompanyService {
    
    SupplierCompanyDTO createCompany(SupplierCompanyDTO companyDTO);
    
    SupplierCompanyDTO getCompanyById(Long companyId);
    
    List<SupplierCompanyDTO> getAllCompanies();
    
    SupplierCompanyDTO updateCompany(Long companyId, SupplierCompanyDTO companyDTO);
    
    void deleteCompany(Long companyId);
}
