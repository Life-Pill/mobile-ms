package com.lifepill.inventoryservice.service;

import com.lifepill.inventoryservice.dto.SupplierDTO;
import com.lifepill.inventoryservice.dto.SupplierRequestDTO;

import java.util.List;

/**
 * Service interface for Supplier operations
 */
public interface SupplierService {
    
    SupplierDTO createSupplier(SupplierRequestDTO requestDTO);
    
    SupplierDTO getSupplierById(Long supplierId);
    
    List<SupplierDTO> getAllSuppliers();
    
    List<SupplierDTO> getActiveSuppliers();
    
    List<SupplierDTO> getSuppliersByCompanyId(Long companyId);
    
    SupplierDTO updateSupplier(Long supplierId, SupplierRequestDTO requestDTO);
    
    void deleteSupplier(Long supplierId);
    
    SupplierDTO updateSupplierStatus(Long supplierId, boolean status);
}
