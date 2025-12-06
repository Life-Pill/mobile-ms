package com.lifepill.branchservice.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for changing branch manager.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBranchManagerRequestDTO {
    private Long formerManagerId;
    private Long branchId;
    private Long newManagerId;
    private String currentManagerNewRole;
    private String newManagerRole;
}
