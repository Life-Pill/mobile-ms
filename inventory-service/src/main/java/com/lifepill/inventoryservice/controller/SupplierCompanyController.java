package com.lifepill.inventoryservice.controller;

import com.lifepill.inventoryservice.dto.ApiResponse;
import com.lifepill.inventoryservice.dto.SupplierCompanyDTO;
import com.lifepill.inventoryservice.service.SupplierCompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for SupplierCompany operations
 */
@RestController
@RequestMapping("/lifepill/v1/supplier-company")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Supplier Company Management", description = "APIs for managing supplier companies")
@CrossOrigin(origins = "*")
public class SupplierCompanyController {
    
    private final SupplierCompanyService companyService;
    
    @PostMapping("/save")
    @Operation(summary = "Create supplier company", description = "Create a new supplier company")
    public ResponseEntity<ApiResponse<SupplierCompanyDTO>> createCompany(@RequestBody SupplierCompanyDTO companyDTO) {
        log.info("Creating new supplier company: {}", companyDTO.getCompanyName());
        SupplierCompanyDTO createdCompany = companyService.createCompany(companyDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdCompany));
    }
    
    @GetMapping("/get-all")
    @Operation(summary = "Get all supplier companies", description = "Retrieve all supplier companies")
    public ResponseEntity<ApiResponse<List<SupplierCompanyDTO>>> getAllCompanies() {
        log.info("Fetching all supplier companies");
        List<SupplierCompanyDTO> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(ApiResponse.success(companies));
    }
    
    @GetMapping("/get-by-id/{companyId}")
    @Operation(summary = "Get supplier company by ID", description = "Retrieve a supplier company by its ID")
    public ResponseEntity<ApiResponse<SupplierCompanyDTO>> getCompanyById(@PathVariable Long companyId) {
        log.info("Fetching supplier company with ID: {}", companyId);
        SupplierCompanyDTO company = companyService.getCompanyById(companyId);
        return ResponseEntity.ok(ApiResponse.success(company));
    }
    
    @PutMapping("/update/{companyId}")
    @Operation(summary = "Update supplier company", description = "Update an existing supplier company")
    public ResponseEntity<ApiResponse<SupplierCompanyDTO>> updateCompany(
            @PathVariable Long companyId,
            @RequestBody SupplierCompanyDTO companyDTO) {
        log.info("Updating supplier company with ID: {}", companyId);
        SupplierCompanyDTO updatedCompany = companyService.updateCompany(companyId, companyDTO);
        return ResponseEntity.ok(ApiResponse.success("Supplier company updated successfully", updatedCompany));
    }
    
    @DeleteMapping("/delete/{companyId}")
    @Operation(summary = "Delete supplier company", description = "Delete a supplier company by its ID")
    public ResponseEntity<ApiResponse<String>> deleteCompany(@PathVariable Long companyId) {
        log.info("Deleting supplier company with ID: {}", companyId);
        companyService.deleteCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Supplier company deleted successfully", null));
    }
}
