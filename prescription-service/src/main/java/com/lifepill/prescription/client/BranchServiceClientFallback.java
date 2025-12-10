package com.lifepill.prescription.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Fallback for BranchServiceClient when branch service is unavailable.
 */
@Component
@Slf4j
public class BranchServiceClientFallback implements BranchServiceClient {
    
    @Override
    public BranchDetailsDTO getBranchById(Long branchId) {
        log.warn("Branch service unavailable, returning fallback for branchId: {}", branchId);
        return BranchDetailsDTO.builder()
                .branchId(branchId)
                .branchName("Unknown Branch")
                .branchAddress("Address unavailable")
                .build();
    }
}
