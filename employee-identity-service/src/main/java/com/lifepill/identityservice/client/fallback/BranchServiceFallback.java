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
        log.warn("Branch Service is unavailable. Fallback called for branchExists({})", branchId);
        return ResponseEntity.ok(
                MicroserviceApiResponse.<Boolean>builder()
                        .code(503)
                        .message("Branch Service is unavailable")
                        .data(false)
                        .build()
        );
    }
}
