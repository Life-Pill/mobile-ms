package com.lifepill.branchservice.service;

import com.lifepill.branchservice.dto.manager.*;

import java.util.List;

/**
 * Service interface for Branch Manager operations.
 */
public interface BranchManagerService {

    /**
     * Get all managers for a branch.
     * @param branchId the branch ID
     * @return list of managers
     */
    List<BranchManagerDTO> getManagersByBranch(Long branchId);

    /**
     * Get employers by branch and role.
     * @param branchId the branch ID
     * @param role the role to filter
     * @return list of employers with specified role
     */
    List<BranchManagerDTO> getEmployersByBranchAndRole(Long branchId, String role);

    /**
     * Update or create a branch manager.
     * @param branchId the branch ID
     * @param requestDTO the manager data
     * @return updated/created manager
     */
    BranchManagerDTO updateOrCreateManager(Long branchId, UpdateManagerRequestDTO requestDTO);

    /**
     * Change the branch manager.
     * @param requestDTO the change request data
     * @return response with new and former manager details
     */
    ChangeBranchManagerResponseDTO changeBranchManager(ChangeBranchManagerRequestDTO requestDTO);

    /**
     * Get all cashiers by branch ID.
     * @param branchId the branch ID
     * @return list of cashiers
     */
    List<BranchManagerDTO> getCashiersByBranch(Long branchId);
}
