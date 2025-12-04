package com.lifepill.inventoryservice.service.impl;

import com.lifepill.inventoryservice.dto.SupplierDTO;
import com.lifepill.inventoryservice.dto.SupplierRequestDTO;
import com.lifepill.inventoryservice.entity.Supplier;
import com.lifepill.inventoryservice.entity.SupplierCompany;
import com.lifepill.inventoryservice.exception.ResourceAlreadyExistsException;
import com.lifepill.inventoryservice.exception.ResourceNotFoundException;
import com.lifepill.inventoryservice.repository.SupplierCompanyRepository;
import com.lifepill.inventoryservice.repository.SupplierRepository;
import com.lifepill.inventoryservice.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of SupplierService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SupplierServiceImpl implements SupplierService {
    
    private final SupplierRepository supplierRepository;
    private final SupplierCompanyRepository companyRepository;
    
    @Override
    public SupplierDTO createSupplier(SupplierRequestDTO requestDTO) {
        log.info("Creating new supplier: {}", requestDTO.getSupplierName());
        
        if (supplierRepository.existsBySupplierEmail(requestDTO.getSupplierEmail())) {
            throw new ResourceAlreadyExistsException("Supplier", "email", requestDTO.getSupplierEmail());
        }
        
        SupplierCompany company = null;
        if (requestDTO.getCompanyId() != null) {
            company = companyRepository.findById(requestDTO.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("SupplierCompany", "id", requestDTO.getCompanyId()));
        }
        
        Supplier supplier = Supplier.builder()
                .supplierName(requestDTO.getSupplierName())
                .supplierAddress(requestDTO.getSupplierAddress())
                .supplierPhone(requestDTO.getSupplierPhone())
                .supplierEmail(requestDTO.getSupplierEmail())
                .supplierDescription(requestDTO.getSupplierDescription())
                .supplierRating(requestDTO.getSupplierRating())
                .supplierCompany(company)
                .isActive(true)
                .build();
        
        Supplier savedSupplier = supplierRepository.save(supplier);
        log.info("Supplier created successfully with ID: {}", savedSupplier.getSupplierId());
        
        return mapToDTO(savedSupplier);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SupplierDTO getSupplierById(Long supplierId) {
        log.info("Fetching supplier with ID: {}", supplierId);
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", supplierId));
        return mapToDTO(supplier);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierDTO> getAllSuppliers() {
        log.info("Fetching all suppliers");
        return supplierRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierDTO> getActiveSuppliers() {
        log.info("Fetching active suppliers");
        return supplierRepository.findByIsActive(true).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierDTO> getSuppliersByCompanyId(Long companyId) {
        log.info("Fetching suppliers for company ID: {}", companyId);
        return supplierRepository.findBySupplierCompanyCompanyId(companyId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public SupplierDTO updateSupplier(Long supplierId, SupplierRequestDTO requestDTO) {
        log.info("Updating supplier with ID: {}", supplierId);
        
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", supplierId));
        
        if (requestDTO.getCompanyId() != null) {
            SupplierCompany company = companyRepository.findById(requestDTO.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("SupplierCompany", "id", requestDTO.getCompanyId()));
            supplier.setSupplierCompany(company);
        }
        
        if (requestDTO.getSupplierName() != null) supplier.setSupplierName(requestDTO.getSupplierName());
        if (requestDTO.getSupplierAddress() != null) supplier.setSupplierAddress(requestDTO.getSupplierAddress());
        if (requestDTO.getSupplierPhone() != null) supplier.setSupplierPhone(requestDTO.getSupplierPhone());
        if (requestDTO.getSupplierEmail() != null) supplier.setSupplierEmail(requestDTO.getSupplierEmail());
        if (requestDTO.getSupplierDescription() != null) supplier.setSupplierDescription(requestDTO.getSupplierDescription());
        if (requestDTO.getSupplierRating() != null) supplier.setSupplierRating(requestDTO.getSupplierRating());
        
        Supplier updatedSupplier = supplierRepository.save(supplier);
        log.info("Supplier updated successfully with ID: {}", updatedSupplier.getSupplierId());
        
        return mapToDTO(updatedSupplier);
    }
    
    @Override
    public void deleteSupplier(Long supplierId) {
        log.info("Deleting supplier with ID: {}", supplierId);
        
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", supplierId));
        
        supplierRepository.delete(supplier);
        log.info("Supplier deleted successfully with ID: {}", supplierId);
    }
    
    @Override
    public SupplierDTO updateSupplierStatus(Long supplierId, boolean status) {
        log.info("Updating supplier status for ID: {} to {}", supplierId, status);
        
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", supplierId));
        
        supplier.setActive(status);
        Supplier updatedSupplier = supplierRepository.save(supplier);
        
        return mapToDTO(updatedSupplier);
    }
    
    private SupplierDTO mapToDTO(Supplier supplier) {
        SupplierDTO.SupplierDTOBuilder builder = SupplierDTO.builder()
                .supplierId(supplier.getSupplierId())
                .supplierName(supplier.getSupplierName())
                .supplierAddress(supplier.getSupplierAddress())
                .supplierPhone(supplier.getSupplierPhone())
                .supplierEmail(supplier.getSupplierEmail())
                .supplierDescription(supplier.getSupplierDescription())
                .supplierImage(supplier.getSupplierImage())
                .supplierRating(supplier.getSupplierRating())
                .isActive(supplier.isActive());
        
        if (supplier.getSupplierCompany() != null) {
            builder.companyId(supplier.getSupplierCompany().getCompanyId())
                    .companyName(supplier.getSupplierCompany().getCompanyName());
        }
        
        return builder.build();
    }
}
