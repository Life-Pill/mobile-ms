package com.lifepill.branchservice.client;

import com.lifepill.branchservice.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Fallback implementation for OrderServiceClient
 * Provides default responses when Order Service is unavailable
 */
@Component
@Slf4j
public class OrderServiceClientFallback implements OrderServiceClient {

    @Override
    public ResponseEntity<ApiResponse<List<BranchSalesDTO>>> getSalesByBranch() {
        log.warn("Fallback: Order Service unavailable - getSalesByBranch");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(503, "Order Service unavailable", Collections.emptyList()));
    }

    @Override
    public ResponseEntity<ApiResponse<BranchSalesDTO>> getSalesForBranch(Long branchId) {
        log.warn("Fallback: Order Service unavailable - getSalesForBranch for branch: {}", branchId);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(503, "Order Service unavailable", 
                new BranchSalesDTO(branchId, 0.0, 0L, null, null)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<BranchSalesDTO>>> getSalesByPeriod(String startDate, String endDate) {
        log.warn("Fallback: Order Service unavailable - getSalesByPeriod from {} to {}", startDate, endDate);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(503, "Order Service unavailable", Collections.emptyList()));
    }

    @Override
    public ResponseEntity<ApiResponse<List<BranchSalesDTO>>> getSalesByDate(String date) {
        log.warn("Fallback: Order Service unavailable - getSalesByDate for: {}", date);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(503, "Order Service unavailable", Collections.emptyList()));
    }

    @Override
    public ResponseEntity<ApiResponse<List<BranchSalesDTO>>> getMonthlySales(int month, int year) {
        log.warn("Fallback: Order Service unavailable - getMonthlySales for {}/{}", month, year);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(503, "Order Service unavailable", Collections.emptyList()));
    }

    @Override
    public ResponseEntity<ApiResponse<List<BranchSalesDTO>>> getYearlySales(int year) {
        log.warn("Fallback: Order Service unavailable - getYearlySales for {}", year);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(503, "Order Service unavailable", Collections.emptyList()));
    }

    @Override
    public ResponseEntity<ApiResponse<Double>> getTotalSales() {
        log.warn("Fallback: Order Service unavailable - getTotalSales");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(503, "Order Service unavailable", 0.0));
    }

    @Override
    public ResponseEntity<ApiResponse<Long>> getTotalOrderCount() {
        log.warn("Fallback: Order Service unavailable - getTotalOrderCount");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(503, "Order Service unavailable", 0L));
    }

    @Override
    public ResponseEntity<ApiResponse<List<DailySalesSummaryDTO>>> getDailySalesByBranch(Long branchId) {
        log.warn("Fallback: Order Service unavailable - getDailySalesByBranch for branch: {}", branchId);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(503, "Order Service unavailable", Collections.emptyList()));
    }

    @Override
    public ResponseEntity<ApiResponse<List<BranchDailySalesSummaryDTO>>> getAllDailySales() {
        log.warn("Fallback: Order Service unavailable - getAllDailySales");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(503, "Order Service unavailable", Collections.emptyList()));
    }
}
