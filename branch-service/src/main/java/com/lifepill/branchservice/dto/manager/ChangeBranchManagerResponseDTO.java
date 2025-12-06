package com.lifepill.branchservice.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for change branch manager operation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBranchManagerResponseDTO {
    private Long branchId;
    private BranchManagerDTO newManager;
    private BranchManagerDTO formerManager;
}
