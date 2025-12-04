package com.lifepill.branchservice.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for aggregated summary of all pharmacy branches
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllPharmacySummaryResponseDTO {
    
    // Overall totals
    private Long totalBranches;
    private Long activeBranches;
    private Double totalSalesAllBranches;
    private Long totalOrdersAllBranches;
    private Long totalEmployeesAllBranches;
    private Long totalItemsAllBranches;
    
    // Individual branch summaries
    private List<PharmacyBranchSummaryDTO> branchSummaries;
}
