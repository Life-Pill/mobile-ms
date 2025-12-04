package com.lifepill.branchservice.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for branch daily sales summary
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BranchDailySalesDTO {
    
    private Long branchId;
    private String branchName;
    private List<DailySalesEntryDTO> dailySales;
    
    /**
     * Inner DTO for individual daily sales entry
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailySalesEntryDTO {
        private LocalDate date;
        private Long orderCount;
        private Double totalSales;
    }
}
