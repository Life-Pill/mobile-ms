package com.lifepill.branchservice.client;

import com.lifepill.branchservice.dto.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * Feign client for Order Service communication (ORDER-SERVICE)
 * Used to fetch sales and order data for branch summaries
 * Uses Circuit Breaker and Retry patterns for resilience
 */
@FeignClient(
    name = "ORDER-SERVICE",
    fallback = OrderServiceClientFallback.class
)
public interface OrderServiceClient {

    /**
     * Get sales summary for all branches
     * @return list of sales data grouped by branch
     */
    @GetMapping("/lifepill/v1/sales/by-branch")
    @CircuitBreaker(name = "orderService", fallbackMethod = "getSalesByBranchFallback")
    @Retry(name = "orderService")
    ResponseEntity<ApiResponse<List<BranchSalesDTO>>> getSalesByBranch();

    /**
     * Get sales for a specific branch
     * @param branchId the branch ID
     * @return sales data for the branch
     */
    @GetMapping("/lifepill/v1/sales/by-branch/{branchId}")
    @CircuitBreaker(name = "orderService", fallbackMethod = "getSalesForBranchFallback")
    @Retry(name = "orderService")
    ResponseEntity<ApiResponse<BranchSalesDTO>> getSalesForBranch(@PathVariable Long branchId);

    /**
     * Get sales by date range grouped by branch
     * @param startDate the start date
     * @param endDate the end date
     * @return sales data for the period
     */
    @GetMapping("/lifepill/v1/sales/by-period")
    @CircuitBreaker(name = "orderService", fallbackMethod = "getSalesByPeriodFallback")
    @Retry(name = "orderService")
    ResponseEntity<ApiResponse<List<BranchSalesDTO>>> getSalesByPeriod(
        @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate
    );

    /**
     * Get sales by specific date for all branches
     * @param date the date
     * @return sales data for the date
     */
    @GetMapping("/lifepill/v1/sales/by-date")
    @CircuitBreaker(name = "orderService", fallbackMethod = "getSalesByDateFallback")
    @Retry(name = "orderService")
    ResponseEntity<ApiResponse<List<BranchSalesDTO>>> getSalesByDate(
        @RequestParam("date") String date
    );

    /**
     * Get monthly sales summary
     * @param month the month (1-12)
     * @param year the year
     * @return monthly sales data
     */
    @GetMapping("/lifepill/v1/sales/monthly")
    @CircuitBreaker(name = "orderService", fallbackMethod = "getMonthlySalesFallback")
    @Retry(name = "orderService")
    ResponseEntity<ApiResponse<List<BranchSalesDTO>>> getMonthlySales(
        @RequestParam("month") int month,
        @RequestParam("year") int year
    );

    /**
     * Get yearly sales summary
     * @param year the year
     * @return yearly sales data
     */
    @GetMapping("/lifepill/v1/sales/yearly")
    @CircuitBreaker(name = "orderService", fallbackMethod = "getYearlySalesFallback")
    @Retry(name = "orderService")
    ResponseEntity<ApiResponse<List<BranchSalesDTO>>> getYearlySales(@RequestParam("year") int year);

    /**
     * Get total sales across all branches
     * @return total sales amount
     */
    @GetMapping("/lifepill/v1/sales/total")
    @CircuitBreaker(name = "orderService", fallbackMethod = "getTotalSalesFallback")
    @Retry(name = "orderService")
    ResponseEntity<ApiResponse<Double>> getTotalSales();

    /**
     * Get total order count
     * @return total order count
     */
    @GetMapping("/lifepill/v1/sales/count")
    @CircuitBreaker(name = "orderService", fallbackMethod = "getTotalOrderCountFallback")
    @Retry(name = "orderService")
    ResponseEntity<ApiResponse<Long>> getTotalOrderCount();

    /**
     * Get daily sales summary for a specific branch
     * @param branchId the branch ID
     * @return list of daily sales summaries
     */
    @GetMapping("/lifepill/v1/sales/daily/{branchId}")
    @CircuitBreaker(name = "orderService", fallbackMethod = "getDailySalesByBranchFallback")
    @Retry(name = "orderService")
    ResponseEntity<ApiResponse<List<DailySalesSummaryDTO>>> getDailySalesByBranch(@PathVariable Long branchId);

    /**
     * Get daily sales summary for all branches
     * @return list of all daily sales summaries
     */
    @GetMapping("/lifepill/v1/sales/daily-all")
    @CircuitBreaker(name = "orderService", fallbackMethod = "getAllDailySalesFallback")
    @Retry(name = "orderService")
    ResponseEntity<ApiResponse<List<BranchDailySalesSummaryDTO>>> getAllDailySales();

    /**
     * DTO for branch sales data from Order Service
     */
    record BranchSalesDTO(
        Long branchId,
        Double totalSales,
        Long orderCount,
        Date startDate,
        Date endDate
    ) {}

    /**
     * DTO for daily sales summary from Order Service
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    class DailySalesSummaryDTO {
        private java.time.LocalDate date;
        private long orders;
        private double sales;
    }

    /**
     * DTO for branch daily sales summary from Order Service
     * Note: JSON field name is "dailySalesSummary" from Order Service
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    class BranchDailySalesSummaryDTO {
        private Long branchId;
        @com.fasterxml.jackson.annotation.JsonProperty("dailySalesSummary")
        private List<DailySalesSummaryDTO> dailySalesSummary;
    }
}
