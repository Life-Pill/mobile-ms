package com.lifepill.identityservice.client.fallback;

import com.lifepill.identityservice.client.BranchServiceClient;
import com.lifepill.identityservice.client.dto.MicroserviceApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Fallback implementation for BranchServiceClient.
 */
@Component
@Slf4j
public class BranchServiceFallback implements BranchServiceClient {

    @Override
    public ResponseEntity<MicroserviceApiResponse<Boolean>> branchExists(Long branchId) {
        log.warn("Branch Service is unavailable - using fallback response");
        return ResponseEntity.ok(
                new MicroserviceApiResponse<Boolean>(503, "Branch Service unavailable", false)
        );
    }

    @Override
    public ResponseEntity<MicroserviceApiResponse<Object>> getBranchById(Long branchId) {
        log.warn("Branch Service is unavailable - cannot fetch branch details for ID: {}", branchId);
        return ResponseEntity.ok(
                new MicroserviceApiResponse<Object>(503, "Branch Service unavailable", null)
        );
    }
}
