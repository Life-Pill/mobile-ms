package com.lifepill.inventoryservice.service.impl;

import com.lifepill.inventoryservice.dto.SupplierCompanyDTO;
import com.lifepill.inventoryservice.entity.SupplierCompany;
import com.lifepill.inventoryservice.exception.ResourceAlreadyExistsException;
import com.lifepill.inventoryservice.exception.ResourceNotFoundException;
import com.lifepill.inventoryservice.repository.SupplierCompanyRepository;
import com.lifepill.inventoryservice.service.SupplierCompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of SupplierCompanyService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SupplierCompanyServiceImpl implements SupplierCompanyService {
    
    private final SupplierCompanyRepository companyRepository;
    
    @Override
    public SupplierCompanyDTO createCompany(SupplierCompanyDTO companyDTO) {
        log.info("Creating new supplier company: {}", companyDTO.getCompanyName());
        
        if (companyRepository.existsByCompanyEmail(companyDTO.getCompanyEmail())) {
            throw new ResourceAlreadyExistsException("SupplierCompany", "email", companyDTO.getCompanyEmail());
        }
        
        SupplierCompany company = SupplierCompany.builder()
                .companyName(companyDTO.getCompanyName())
                .companyAddress(companyDTO.getCompanyAddress())
                .companyContact(companyDTO.getCompanyContact())
                .companyEmail(companyDTO.getCompanyEmail())
                .companyDescription(companyDTO.getCompanyDescription())
                .companyStatus(companyDTO.getCompanyStatus())
                .companyRating(companyDTO.getCompanyRating())
                .companyBank(companyDTO.getCompanyBank())
                .companyAccountNumber(companyDTO.getCompanyAccountNumber())
                .companyImage(companyDTO.getCompanyImage())
                .build();
        
        SupplierCompany savedCompany = companyRepository.save(company);
        log.info("Supplier company created successfully with ID: {}", savedCompany.getCompanyId());
        
        return mapToDTO(savedCompany);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SupplierCompanyDTO getCompanyById(Long companyId) {
        log.info("Fetching supplier company with ID: {}", companyId);
        SupplierCompany company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierCompany", "id", companyId));
        return mapToDTO(company);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierCompanyDTO> getAllCompanies() {
        log.info("Fetching all supplier companies");
        return companyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public SupplierCompanyDTO updateCompany(Long companyId, SupplierCompanyDTO companyDTO) {
        log.info("Updating supplier company with ID: {}", companyId);
        
        SupplierCompany company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierCompany", "id", companyId));
        
        if (companyDTO.getCompanyName() != null) company.setCompanyName(companyDTO.getCompanyName());
        if (companyDTO.getCompanyAddress() != null) company.setCompanyAddress(companyDTO.getCompanyAddress());
        if (companyDTO.getCompanyContact() != null) company.setCompanyContact(companyDTO.getCompanyContact());
        if (companyDTO.getCompanyEmail() != null) company.setCompanyEmail(companyDTO.getCompanyEmail());
        if (companyDTO.getCompanyDescription() != null) company.setCompanyDescription(companyDTO.getCompanyDescription());
        if (companyDTO.getCompanyStatus() != null) company.setCompanyStatus(companyDTO.getCompanyStatus());
        if (companyDTO.getCompanyRating() != null) company.setCompanyRating(companyDTO.getCompanyRating());
        if (companyDTO.getCompanyBank() != null) company.setCompanyBank(companyDTO.getCompanyBank());
        if (companyDTO.getCompanyAccountNumber() != null) company.setCompanyAccountNumber(companyDTO.getCompanyAccountNumber());
        if (companyDTO.getCompanyImage() != null) company.setCompanyImage(companyDTO.getCompanyImage());
        
        SupplierCompany updatedCompany = companyRepository.save(company);
        log.info("Supplier company updated successfully with ID: {}", updatedCompany.getCompanyId());
        
        return mapToDTO(updatedCompany);
    }
    
    @Override
    public void deleteCompany(Long companyId) {
        log.info("Deleting supplier company with ID: {}", companyId);
        
        SupplierCompany company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierCompany", "id", companyId));
        
        companyRepository.delete(company);
        log.info("Supplier company deleted successfully with ID: {}", companyId);
    }
    
    private SupplierCompanyDTO mapToDTO(SupplierCompany company) {
        return SupplierCompanyDTO.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .companyAddress(company.getCompanyAddress())
                .companyContact(company.getCompanyContact())
                .companyEmail(company.getCompanyEmail())
                .companyDescription(company.getCompanyDescription())
                .companyStatus(company.getCompanyStatus())
                .companyRating(company.getCompanyRating())
                .companyBank(company.getCompanyBank())
                .companyAccountNumber(company.getCompanyAccountNumber())
                .companyImage(company.getCompanyImage())
                .build();
    }
}
