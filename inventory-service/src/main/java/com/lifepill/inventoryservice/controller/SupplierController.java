package com.lifepill.inventoryservice.controller;

import com.lifepill.inventoryservice.dto.ApiResponse;
import com.lifepill.inventoryservice.dto.SupplierDTO;
import com.lifepill.inventoryservice.dto.SupplierRequestDTO;
import com.lifepill.inventoryservice.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Supplier operations
 */
@RestController
@RequestMapping("/lifepill/v1/supplier")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Supplier Management", description = "APIs for managing suppliers")
@CrossOrigin(origins = "*")
public class SupplierController {
    
    private final SupplierService supplierService;
    
    @PostMapping("/save")
    @Operation(summary = "Create supplier", description = "Create a new supplier")
    public ResponseEntity<ApiResponse<SupplierDTO>> createSupplier(@RequestBody SupplierRequestDTO requestDTO) {
        log.info("Creating new supplier: {}", requestDTO.getSupplierName());
        SupplierDTO createdSupplier = supplierService.createSupplier(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdSupplier));
    }
    
    @GetMapping("/get-all")
    @Operation(summary = "Get all suppliers", description = "Retrieve all suppliers")
    public ResponseEntity<ApiResponse<List<SupplierDTO>>> getAllSuppliers() {
        log.info("Fetching all suppliers");
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers));
    }
    
    @GetMapping("/get-active")
    @Operation(summary = "Get active suppliers", description = "Retrieve all active suppliers")
    public ResponseEntity<ApiResponse<List<SupplierDTO>>> getActiveSuppliers() {
        log.info("Fetching active suppliers");
        List<SupplierDTO> suppliers = supplierService.getActiveSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers));
    }
    
    @GetMapping("/get-by-id/{supplierId}")
    @Operation(summary = "Get supplier by ID", description = "Retrieve a supplier by its ID")
    public ResponseEntity<ApiResponse<SupplierDTO>> getSupplierById(@PathVariable Long supplierId) {
        log.info("Fetching supplier with ID: {}", supplierId);
        SupplierDTO supplier = supplierService.getSupplierById(supplierId);
        return ResponseEntity.ok(ApiResponse.success(supplier));
    }
    
    @GetMapping("/get-by-company/{companyId}")
    @Operation(summary = "Get suppliers by company", description = "Retrieve suppliers for a specific company")
    public ResponseEntity<ApiResponse<List<SupplierDTO>>> getSuppliersByCompany(@PathVariable Long companyId) {
        log.info("Fetching suppliers for company ID: {}", companyId);
        List<SupplierDTO> suppliers = supplierService.getSuppliersByCompanyId(companyId);
        return ResponseEntity.ok(ApiResponse.success(suppliers));
    }
    
    @PutMapping("/update/{supplierId}")
    @Operation(summary = "Update supplier", description = "Update an existing supplier")
    public ResponseEntity<ApiResponse<SupplierDTO>> updateSupplier(
            @PathVariable Long supplierId,
            @RequestBody SupplierRequestDTO requestDTO) {
        log.info("Updating supplier with ID: {}", supplierId);
        SupplierDTO updatedSupplier = supplierService.updateSupplier(supplierId, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Supplier updated successfully", updatedSupplier));
    }
    
    @PutMapping("/update-status/{supplierId}")
    @Operation(summary = "Update supplier status", description = "Update the active status of a supplier")
    public ResponseEntity<ApiResponse<SupplierDTO>> updateSupplierStatus(
            @PathVariable Long supplierId,
            @RequestParam boolean status) {
        log.info("Updating supplier status for ID: {} to {}", supplierId, status);
        SupplierDTO updatedSupplier = supplierService.updateSupplierStatus(supplierId, status);
        return ResponseEntity.ok(ApiResponse.success("Supplier status updated successfully", updatedSupplier));
    }
    
    @DeleteMapping("/delete/{supplierId}")
    @Operation(summary = "Delete supplier", description = "Delete a supplier by its ID")
    public ResponseEntity<ApiResponse<String>> deleteSupplier(@PathVariable Long supplierId) {
        log.info("Deleting supplier with ID: {}", supplierId);
        supplierService.deleteSupplier(supplierId);
        return ResponseEntity.ok(ApiResponse.success("Supplier deleted successfully", null));
    }
}
