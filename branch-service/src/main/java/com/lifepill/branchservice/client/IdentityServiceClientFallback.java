package com.lifepill.branchservice.client;

import com.lifepill.branchservice.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Fallback implementation for Identity Service client.
 * Returns default responses when Identity Service is unavailable.
 */
@Component
@Slf4j
public class IdentityServiceClientFallback implements IdentityServiceClient {

    @Override
    public ResponseEntity<ApiResponse<List<EmployerDTO>>> getEmployersByBranch(Long branchId) {
        log.warn("Fallback: Identity Service unavailable, returning empty employers list for branch {}", branchId);
        return ResponseEntity.ok(
            ApiResponse.<List<EmployerDTO>>builder()
                .code(503)
                .message("Identity Service temporarily unavailable")
                .data(Collections.emptyList())
                .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Long>> countEmployersByBranch(Long branchId) {
        log.warn("Fallback: Identity Service unavailable, returning 0 count for branch {}", branchId);
        return ResponseEntity.ok(
            ApiResponse.<Long>builder()
                .code(503)
                .message("Identity Service temporarily unavailable")
                .data(0L)
                .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<List<EmployerDTO>>> getActiveEmployersByBranch(Long branchId) {
        log.warn("Fallback: Identity Service unavailable, returning empty active employers list for branch {}", branchId);
        return ResponseEntity.ok(
            ApiResponse.<List<EmployerDTO>>builder()
                .code(503)
                .message("Identity Service temporarily unavailable")
                .data(Collections.emptyList())
                .build()
        );
    }
}
