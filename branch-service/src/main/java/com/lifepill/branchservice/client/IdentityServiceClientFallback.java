package com.lifepill.branchservice.client;

import com.lifepill.branchservice.dto.ApiResponse;
import com.lifepill.branchservice.dto.manager.BranchManagerDTO;
import com.lifepill.branchservice.dto.manager.UpdateManagerRequestDTO;
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

    private static final String FALLBACK_MESSAGE = "Identity Service temporarily unavailable";

    @Override
    public ResponseEntity<ApiResponse<List<EmployerDTO>>> getEmployersByBranch(Long branchId) {
        log.warn("Fallback: Identity Service unavailable, returning empty employers list for branch {}", branchId);
        return ResponseEntity.ok(
            ApiResponse.<List<EmployerDTO>>builder()
                .code(503)
                .message(FALLBACK_MESSAGE)
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
                .message(FALLBACK_MESSAGE)
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
                .message(FALLBACK_MESSAGE)
                .data(Collections.emptyList())
                .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<List<BranchManagerDTO>>> getEmployersByBranchAndRole(Long branchId, String role) {
        log.warn("Fallback: Identity Service unavailable for getEmployersByBranchAndRole - branch: {}, role: {}", branchId, role);
        return ResponseEntity.ok(
            ApiResponse.<List<BranchManagerDTO>>builder()
                .code(503)
                .message(FALLBACK_MESSAGE)
                .data(Collections.emptyList())
                .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<List<BranchManagerDTO>>> getManagersByBranch(Long branchId) {
        log.warn("Fallback: Identity Service unavailable for getManagersByBranch - branch: {}", branchId);
        return ResponseEntity.ok(
            ApiResponse.<List<BranchManagerDTO>>builder()
                .code(503)
                .message(FALLBACK_MESSAGE)
                .data(Collections.emptyList())
                .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<BranchManagerDTO>> getEmployerById(Long employerId) {
        log.warn("Fallback: Identity Service unavailable for getEmployerById - employerId: {}", employerId);
        return ResponseEntity.ok(
            ApiResponse.<BranchManagerDTO>builder()
                .code(503)
                .message(FALLBACK_MESSAGE)
                .data(null)
                .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<BranchManagerDTO>> updateEmployer(Long employerId, UpdateManagerRequestDTO updateDTO) {
        log.warn("Fallback: Identity Service unavailable for updateEmployer - employerId: {}", employerId);
        return ResponseEntity.ok(
            ApiResponse.<BranchManagerDTO>builder()
                .code(503)
                .message(FALLBACK_MESSAGE)
                .data(null)
                .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<BranchManagerDTO>> changeEmployerRole(Long employerId, String newRole) {
        log.warn("Fallback: Identity Service unavailable for changeEmployerRole - employerId: {}, newRole: {}", employerId, newRole);
        return ResponseEntity.ok(
            ApiResponse.<BranchManagerDTO>builder()
                .code(503)
                .message(FALLBACK_MESSAGE)
                .data(null)
                .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<BranchManagerDTO>> createEmployer(Long branchId, UpdateManagerRequestDTO createDTO) {
        log.warn("Fallback: Identity Service unavailable for createEmployer - branchId: {}", branchId);
        return ResponseEntity.ok(
            ApiResponse.<BranchManagerDTO>builder()
                .code(503)
                .message(FALLBACK_MESSAGE)
                .data(null)
                .build()
        );
    }
}

