package com.lifepill.branchservice.service.impl;

import com.lifepill.branchservice.client.IdentityServiceClient;
import com.lifepill.branchservice.client.InventoryServiceClient;
import com.lifepill.branchservice.client.OrderServiceClient;
import com.lifepill.branchservice.dto.ApiResponse;
import com.lifepill.branchservice.dto.summary.AllPharmacySummaryResponseDTO;
import com.lifepill.branchservice.dto.summary.BranchDailySalesDTO;
import com.lifepill.branchservice.dto.summary.PharmacyBranchSummaryDTO;
import com.lifepill.branchservice.entity.Branch;
import com.lifepill.branchservice.exception.ResourceNotFoundException;
import com.lifepill.branchservice.repository.BranchRepository;
import com.lifepill.branchservice.service.BranchSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of BranchSummaryService.
 * Aggregates data from Branch entity and other microservices via Feign clients.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BranchSummaryServiceImpl implements BranchSummaryService {

    private final BranchRepository branchRepository;
    private final OrderServiceClient orderServiceClient;
    private final IdentityServiceClient identityServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    @Override
    public AllPharmacySummaryResponseDTO getAllPharmacySummary() {
        log.info("Fetching complete pharmacy summary for all branches");
        
        List<Branch> branches = branchRepository.findAll();
        long activeBranches = branches.stream().filter(Branch::isBranchStatus).count();
        
        // Get sales data from Order Service
        Map<Long, OrderServiceClient.BranchSalesDTO> salesMap = getSalesByBranchMap();
        
        // Build individual branch summaries
        List<PharmacyBranchSummaryDTO> branchSummaries = branches.stream()
                .map(branch -> buildBranchSummary(branch, salesMap.get(branch.getBranchId())))
                .collect(Collectors.toList());
        
        // Calculate totals
        Double totalSales = branchSummaries.stream()
                .mapToDouble(summary -> summary.getTotalSales() != null ? summary.getTotalSales() : 0.0)
                .sum();
        Long totalOrders = branchSummaries.stream()
                .mapToLong(summary -> summary.getOrderCount() != null ? summary.getOrderCount() : 0L)
                .sum();
        Long totalEmployees = branchSummaries.stream()
                .mapToLong(summary -> summary.getEmployeeCount() != null ? summary.getEmployeeCount() : 0L)
                .sum();
        Long totalItems = branchSummaries.stream()
                .mapToLong(summary -> summary.getItemCount() != null ? summary.getItemCount() : 0L)
                .sum();
        
        return AllPharmacySummaryResponseDTO.builder()
                .totalBranches((long) branches.size())
                .activeBranches(activeBranches)
                .totalSalesAllBranches(totalSales)
                .totalOrdersAllBranches(totalOrders)
                .totalEmployeesAllBranches(totalEmployees)
                .totalItemsAllBranches(totalItems)
                .branchSummaries(branchSummaries)
                .build();
    }

    @Override
    public PharmacyBranchSummaryDTO getBranchSummary(Long branchId) {
        log.info("Fetching summary for branch ID: {}", branchId);
        
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "id", branchId));
        
        // Get sales data for specific branch
        OrderServiceClient.BranchSalesDTO salesData = getSalesForBranch(branchId);
        
        return buildBranchSummary(branch, salesData);
    }

    @Override
    public List<BranchDailySalesDTO> getDailySalesSummary() {
        log.info("Fetching daily sales summary for all branches");
        
        List<Branch> branches = branchRepository.findAll();
        
        try {
            ResponseEntity<ApiResponse<List<OrderServiceClient.BranchDailySalesSummaryDTO>>> response = 
                    orderServiceClient.getAllDailySales();
            
            if (response.getBody() != null && response.getBody().getData() != null) {
                Map<Long, OrderServiceClient.BranchDailySalesSummaryDTO> dailySalesMap = 
                        response.getBody().getData().stream()
                                .collect(Collectors.toMap(
                                        OrderServiceClient.BranchDailySalesSummaryDTO::branchId,
                                        dto -> dto
                                ));
                
                return branches.stream()
                        .map(branch -> {
                            OrderServiceClient.BranchDailySalesSummaryDTO dailySales = 
                                    dailySalesMap.get(branch.getBranchId());
                            
                            List<BranchDailySalesDTO.DailySalesEntryDTO> entries = 
                                    dailySales != null && dailySales.dailySales() != null
                                            ? dailySales.dailySales().stream()
                                                    .map(ds -> BranchDailySalesDTO.DailySalesEntryDTO.builder()
                                                            .date(ds.date())
                                                            .orderCount(ds.orders())
                                                            .totalSales(ds.sales())
                                                            .build())
                                                    .collect(Collectors.toList())
                                            : Collections.emptyList();
                            
                            return BranchDailySalesDTO.builder()
                                    .branchId(branch.getBranchId())
                                    .branchName(branch.getBranchName())
                                    .dailySales(entries)
                                    .build();
                        })
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error fetching daily sales from Order Service", e);
        }
        
        // Return empty summaries if Order Service is unavailable
        return branches.stream()
                .map(branch -> BranchDailySalesDTO.builder()
                        .branchId(branch.getBranchId())
                        .branchName(branch.getBranchName())
                        .dailySales(Collections.emptyList())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public BranchDailySalesDTO getDailySalesSummaryForBranch(Long branchId) {
        log.info("Fetching daily sales summary for branch ID: {}", branchId);
        
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "id", branchId));
        
        try {
            ResponseEntity<ApiResponse<List<OrderServiceClient.DailySalesSummaryDTO>>> response = 
                    orderServiceClient.getDailySalesByBranch(branchId);
            
            if (response.getBody() != null && response.getBody().getData() != null) {
                List<BranchDailySalesDTO.DailySalesEntryDTO> entries = 
                        response.getBody().getData().stream()
                                .map(ds -> BranchDailySalesDTO.DailySalesEntryDTO.builder()
                                        .date(ds.date())
                                        .orderCount(ds.orders())
                                        .totalSales(ds.sales())
                                        .build())
                                .collect(Collectors.toList());
                
                return BranchDailySalesDTO.builder()
                        .branchId(branchId)
                        .branchName(branch.getBranchName())
                        .dailySales(entries)
                        .build();
            }
        } catch (Exception e) {
            log.error("Error fetching daily sales for branch {} from Order Service", branchId, e);
        }
        
        return BranchDailySalesDTO.builder()
                .branchId(branchId)
                .branchName(branch.getBranchName())
                .dailySales(Collections.emptyList())
                .build();
    }

    @Override
    public List<PharmacyBranchSummaryDTO> getSalesByPeriod(Date startDate, Date endDate) {
        log.info("Fetching sales by period from {} to {}", startDate, endDate);
        
        List<Branch> branches = branchRepository.findAll();
        Map<Long, OrderServiceClient.BranchSalesDTO> salesMap = new HashMap<>();
        
        try {
            String startStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(startDate);
            String endStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(endDate);
            
            ResponseEntity<ApiResponse<List<OrderServiceClient.BranchSalesDTO>>> response = 
                    orderServiceClient.getSalesByPeriod(startStr, endStr);
            
            if (response.getBody() != null && response.getBody().getData() != null) {
                salesMap = response.getBody().getData().stream()
                        .collect(Collectors.toMap(
                                OrderServiceClient.BranchSalesDTO::branchId,
                                dto -> dto
                        ));
            }
        } catch (Exception e) {
            log.error("Error fetching sales by period from Order Service", e);
        }
        
        Map<Long, OrderServiceClient.BranchSalesDTO> finalSalesMap = salesMap;
        return branches.stream()
                .map(branch -> buildBranchSummary(branch, finalSalesMap.get(branch.getBranchId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<PharmacyBranchSummaryDTO> getMonthlySales(int month, int year) {
        log.info("Fetching monthly sales for {}/{}", month, year);
        
        List<Branch> branches = branchRepository.findAll();
        Map<Long, OrderServiceClient.BranchSalesDTO> salesMap = new HashMap<>();
        
        try {
            ResponseEntity<ApiResponse<List<OrderServiceClient.BranchSalesDTO>>> response = 
                    orderServiceClient.getMonthlySales(month, year);
            
            if (response.getBody() != null && response.getBody().getData() != null) {
                salesMap = response.getBody().getData().stream()
                        .collect(Collectors.toMap(
                                OrderServiceClient.BranchSalesDTO::branchId,
                                dto -> dto
                        ));
            }
        } catch (Exception e) {
            log.error("Error fetching monthly sales from Order Service", e);
        }
        
        Map<Long, OrderServiceClient.BranchSalesDTO> finalSalesMap = salesMap;
        return branches.stream()
                .map(branch -> buildBranchSummary(branch, finalSalesMap.get(branch.getBranchId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<PharmacyBranchSummaryDTO> getYearlySales(int year) {
        log.info("Fetching yearly sales for {}", year);
        
        List<Branch> branches = branchRepository.findAll();
        Map<Long, OrderServiceClient.BranchSalesDTO> salesMap = new HashMap<>();
        
        try {
            ResponseEntity<ApiResponse<List<OrderServiceClient.BranchSalesDTO>>> response = 
                    orderServiceClient.getYearlySales(year);
            
            if (response.getBody() != null && response.getBody().getData() != null) {
                salesMap = response.getBody().getData().stream()
                        .collect(Collectors.toMap(
                                OrderServiceClient.BranchSalesDTO::branchId,
                                dto -> dto
                        ));
            }
        } catch (Exception e) {
            log.error("Error fetching yearly sales from Order Service", e);
        }
        
        Map<Long, OrderServiceClient.BranchSalesDTO> finalSalesMap = salesMap;
        return branches.stream()
                .map(branch -> buildBranchSummary(branch, finalSalesMap.get(branch.getBranchId())))
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalSales() {
        log.info("Fetching total sales");
        
        try {
            ResponseEntity<ApiResponse<Double>> response = orderServiceClient.getTotalSales();
            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData();
            }
        } catch (Exception e) {
            log.error("Error fetching total sales from Order Service", e);
        }
        
        return 0.0;
    }

    @Override
    public Long getTotalOrderCount() {
        log.info("Fetching total order count");
        
        try {
            ResponseEntity<ApiResponse<Long>> response = orderServiceClient.getTotalOrderCount();
            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData();
            }
        } catch (Exception e) {
            log.error("Error fetching total order count from Order Service", e);
        }
        
        return 0L;
    }

    // Helper Methods
    
    private Map<Long, OrderServiceClient.BranchSalesDTO> getSalesByBranchMap() {
        try {
            ResponseEntity<ApiResponse<List<OrderServiceClient.BranchSalesDTO>>> response = 
                    orderServiceClient.getSalesByBranch();
            
            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData().stream()
                        .collect(Collectors.toMap(
                                OrderServiceClient.BranchSalesDTO::branchId,
                                dto -> dto
                        ));
            }
        } catch (Exception e) {
            log.error("Error fetching sales by branch from Order Service", e);
        }
        
        return Collections.emptyMap();
    }
    
    private OrderServiceClient.BranchSalesDTO getSalesForBranch(Long branchId) {
        try {
            ResponseEntity<ApiResponse<OrderServiceClient.BranchSalesDTO>> response = 
                    orderServiceClient.getSalesForBranch(branchId);
            
            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData();
            }
        } catch (Exception e) {
            log.error("Error fetching sales for branch {} from Order Service", branchId, e);
        }
        
        return null;
    }
    
    private Long getEmployeeCountForBranch(Long branchId) {
        try {
            ResponseEntity<ApiResponse<Long>> response = 
                    identityServiceClient.countEmployersByBranch(branchId);
            
            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData();
            }
        } catch (Exception e) {
            log.error("Error fetching employee count for branch {} from Identity Service", branchId, e);
        }
        
        return 0L;
    }
    
    private Long getItemCountForBranch(Long branchId) {
        try {
            ResponseEntity<ApiResponse<Long>> response = 
                    inventoryServiceClient.countItemsByBranch(branchId);
            
            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData();
            }
        } catch (Exception e) {
            log.error("Error fetching item count for branch {} from Inventory Service", branchId, e);
        }
        
        return 0L;
    }
    
    private Long getLowStockItemCountForBranch(Long branchId) {
        try {
            ResponseEntity<ApiResponse<List<InventoryServiceClient.ItemDTO>>> response = 
                    inventoryServiceClient.getLowStockItems(branchId, 10.0);
            
            if (response.getBody() != null && response.getBody().getData() != null) {
                return (long) response.getBody().getData().size();
            }
        } catch (Exception e) {
            log.error("Error fetching low stock items for branch {} from Inventory Service", branchId, e);
        }
        
        return 0L;
    }
    
    private PharmacyBranchSummaryDTO buildBranchSummary(Branch branch, 
                                                        OrderServiceClient.BranchSalesDTO salesData) {
        Long employeeCount = getEmployeeCountForBranch(branch.getBranchId());
        Long itemCount = getItemCountForBranch(branch.getBranchId());
        Long lowStockItemCount = getLowStockItemCountForBranch(branch.getBranchId());
        
        return PharmacyBranchSummaryDTO.builder()
                .branchId(branch.getBranchId())
                .branchName(branch.getBranchName())
                .branchAddress(branch.getBranchAddress())
                .branchContact(branch.getBranchContact())
                .branchEmail(branch.getBranchEmail())
                .branchLocation(branch.getBranchLocation())
                .branchStatus(branch.isBranchStatus())
                .branchImageUrl(branch.getBranchImageUrl())
                .totalSales(salesData != null ? salesData.totalSales() : 0.0)
                .orderCount(salesData != null ? salesData.orderCount() : 0L)
                .employeeCount(employeeCount)
                .itemCount(itemCount)
                .lowStockItemCount(lowStockItemCount)
                .build();
    }
}
