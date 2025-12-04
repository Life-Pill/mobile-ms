package com.lifepill.possystem.client;

import com.lifepill.possystem.client.dto.MicroserviceApiResponse;
import com.lifepill.possystem.client.dto.MicroserviceBranchDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Fallback implementation for Branch Service Client
 * Provides default responses when the Branch Service is unavailable
 */
@Component
public class BranchServiceClientFallback implements BranchServiceClient {

    @Override
    public ResponseEntity<MicroserviceApiResponse<List<MicroserviceBranchDTO>>> getAllBranches() {
        MicroserviceApiResponse<List<MicroserviceBranchDTO>> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Branch Service unavailable");
        response.setData(Collections.emptyList());
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MicroserviceApiResponse<MicroserviceBranchDTO>> getBranchById(Long branchId) {
        MicroserviceApiResponse<MicroserviceBranchDTO> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Branch Service unavailable");
        response.setData(createDefaultBranch(branchId));
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MicroserviceApiResponse<Boolean>> branchExists(Long branchId) {
        MicroserviceApiResponse<Boolean> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Branch Service unavailable - assuming branch exists");
        response.setData(true); // Assume exists when service is down
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MicroserviceApiResponse<Long>> countBranches() {
        MicroserviceApiResponse<Long> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Branch Service unavailable");
        response.setData(0L);
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    private MicroserviceBranchDTO createDefaultBranch(Long branchId) {
        MicroserviceBranchDTO branch = new MicroserviceBranchDTO();
        branch.setBranchId(branchId);
        branch.setBranchName("Branch " + branchId);
        return branch;
    }
}
