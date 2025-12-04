package com.lifepill.possystem.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for branch sales summary data exposed to other microservices
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BranchSalesDTO {
    private Long branchId;
    private Double totalSales;
    private Long orderCount;
}
