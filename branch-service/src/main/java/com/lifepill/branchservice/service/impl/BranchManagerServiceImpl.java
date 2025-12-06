package com.lifepill.branchservice.service.impl;

import com.lifepill.branchservice.client.IdentityServiceClient;
import com.lifepill.branchservice.dto.ApiResponse;
import com.lifepill.branchservice.dto.manager.*;
import com.lifepill.branchservice.exception.ResourceNotFoundException;
import com.lifepill.branchservice.service.BranchManagerService;
import com.lifepill.branchservice.service.BranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of Branch Manager Service.
 * Handles branch manager operations by communicating with Identity Service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BranchManagerServiceImpl implements BranchManagerService {

    private final IdentityServiceClient identityServiceClient;
    private final BranchService branchService;

    @Override
    public List<BranchManagerDTO> getManagersByBranch(Long branchId) {
        log.info("Getting managers for branch ID: {}", branchId);
        
        // Verify branch exists
        if (!branchService.branchExists(branchId)) {
            throw new ResourceNotFoundException("Branch not found with ID: " + branchId);
        }

        ResponseEntity<ApiResponse<List<BranchManagerDTO>>> response = 
            identityServiceClient.getManagersByBranch(branchId);
        
        if (response.getBody() != null && response.getBody().getCode() == 200) {
            return response.getBody().getData();
        }
        
        log.warn("Failed to get managers from Identity Service for branch: {}", branchId);
        return Collections.emptyList();
    }

    @Override
    public List<BranchManagerDTO> getEmployersByBranchAndRole(Long branchId, String role) {
        log.info("Getting employers for branch ID: {} with role: {}", branchId, role);
        
        // Verify branch exists
        if (!branchService.branchExists(branchId)) {
            throw new ResourceNotFoundException("Branch not found with ID: " + branchId);
        }

        ResponseEntity<ApiResponse<List<BranchManagerDTO>>> response = 
            identityServiceClient.getEmployersByBranchAndRole(branchId, role);
        
        if (response.getBody() != null && response.getBody().getCode() == 200) {
            return response.getBody().getData();
        }
        
        log.warn("Failed to get employers from Identity Service for branch: {} role: {}", branchId, role);
        return Collections.emptyList();
    }

    @Override
    public BranchManagerDTO updateOrCreateManager(Long branchId, UpdateManagerRequestDTO requestDTO) {
        log.info("Update or create manager for branch ID: {}", branchId);
        
        // Verify branch exists
        if (!branchService.branchExists(branchId)) {
            throw new ResourceNotFoundException("Branch not found with ID: " + branchId);
        }

        // Try to create a new employer
        ResponseEntity<ApiResponse<BranchManagerDTO>> response = 
            identityServiceClient.createEmployer(branchId, requestDTO);
        
        if (response.getBody() != null && response.getBody().getCode() == 200) {
            log.info("Successfully created manager for branch: {}", branchId);
            return response.getBody().getData();
        }
        
        log.error("Failed to create manager for branch: {}", branchId);
        throw new RuntimeException("Failed to create manager in Identity Service");
    }

    @Override
    public ChangeBranchManagerResponseDTO changeBranchManager(ChangeBranchManagerRequestDTO requestDTO) {
        log.info("Changing branch manager for branch ID: {}", requestDTO.getBranchId());
        
        // Verify branch exists
        if (!branchService.branchExists(requestDTO.getBranchId())) {
            throw new ResourceNotFoundException("Branch not found with ID: " + requestDTO.getBranchId());
        }

        // Get former manager details
        ResponseEntity<ApiResponse<BranchManagerDTO>> formerManagerResponse = 
            identityServiceClient.getEmployerById(requestDTO.getFormerManagerId());
        
        BranchManagerDTO formerManager = null;
        if (formerManagerResponse.getBody() != null && formerManagerResponse.getBody().getCode() == 200) {
            formerManager = formerManagerResponse.getBody().getData();
        }

        // Get new manager details
        ResponseEntity<ApiResponse<BranchManagerDTO>> newManagerResponse = 
            identityServiceClient.getEmployerById(requestDTO.getNewManagerId());
        
        BranchManagerDTO newManager = null;
        if (newManagerResponse.getBody() != null && newManagerResponse.getBody().getCode() == 200) {
            newManager = newManagerResponse.getBody().getData();
        }

        // Change former manager's role
        if (requestDTO.getCurrentManagerNewRole() != null && requestDTO.getFormerManagerId() != null) {
            identityServiceClient.changeEmployerRole(
                requestDTO.getFormerManagerId(), 
                requestDTO.getCurrentManagerNewRole()
            );
        }

        // Change new manager's role
        if (requestDTO.getNewManagerRole() != null && requestDTO.getNewManagerId() != null) {
            ResponseEntity<ApiResponse<BranchManagerDTO>> updatedNewManagerResponse = 
                identityServiceClient.changeEmployerRole(
                    requestDTO.getNewManagerId(), 
                    requestDTO.getNewManagerRole()
                );
            
            if (updatedNewManagerResponse.getBody() != null && updatedNewManagerResponse.getBody().getCode() == 200) {
                newManager = updatedNewManagerResponse.getBody().getData();
            }
        }

        log.info("Successfully changed branch manager for branch: {}", requestDTO.getBranchId());
        
        return ChangeBranchManagerResponseDTO.builder()
            .branchId(requestDTO.getBranchId())
            .formerManager(formerManager)
            .newManager(newManager)
            .build();
    }

    @Override
    public List<BranchManagerDTO> getCashiersByBranch(Long branchId) {
        log.info("Getting cashiers for branch ID: {}", branchId);
        return getEmployersByBranchAndRole(branchId, "CASHIER");
    }
}
