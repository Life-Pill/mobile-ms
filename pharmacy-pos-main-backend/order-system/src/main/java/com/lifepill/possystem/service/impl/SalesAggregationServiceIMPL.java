package com.lifepill.possystem.service.impl;

import com.lifepill.possystem.dto.requestDTO.BranchDailySalesSummaryDTO;
import com.lifepill.possystem.dto.responseDTO.BranchSalesDTO;
import com.lifepill.possystem.dto.responseDTO.DailySalesSummaryDTO;
import com.lifepill.possystem.entity.Order;
import com.lifepill.possystem.repo.orderRepository.OrderRepository;
import com.lifepill.possystem.service.SalesAggregationService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of SalesAggregationService.
 * Provides sales aggregation data for other microservices via Feign clients.
 */
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class SalesAggregationServiceIMPL implements SalesAggregationService {

    private final OrderRepository orderRepository;

    @Override
    public List<BranchSalesDTO> getSalesByBranch() {
        List<Order> allOrders = orderRepository.findAll();
        
        Map<Long, Double> branchSalesMap = allOrders.stream()
                .collect(Collectors.groupingBy(
                        Order::getBranchId,
                        Collectors.summingDouble(Order::getTotal)
                ));
        
        Map<Long, Long> branchOrdersCountMap = allOrders.stream()
                .collect(Collectors.groupingBy(
                        Order::getBranchId,
                        Collectors.counting()
                ));
        
        return branchSalesMap.entrySet().stream()
                .map(entry -> BranchSalesDTO.builder()
                        .branchId(entry.getKey())
                        .totalSales(entry.getValue())
                        .orderCount(branchOrdersCountMap.getOrDefault(entry.getKey(), 0L))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public BranchSalesDTO getSalesForBranch(Long branchId) {
        List<Order> branchOrders = orderRepository.findByBranchId(branchId);
        
        Double totalSales = branchOrders.stream()
                .mapToDouble(Order::getTotal)
                .sum();
        
        return BranchSalesDTO.builder()
                .branchId(branchId)
                .totalSales(totalSales)
                .orderCount((long) branchOrders.size())
                .build();
    }

    @Override
    public List<BranchSalesDTO> getSalesByPeriod(Date startDate, Date endDate) {
        List<Order> ordersForPeriod = orderRepository.findByOrderDateBetween(startDate, endDate);
        
        return aggregateOrdersByBranch(ordersForPeriod);
    }

    @Override
    public List<BranchSalesDTO> getSalesByDate(Date date) {
        List<Order> ordersForDate = orderRepository.findByOrderDateBetween(
                DateUtils.truncate(date, Calendar.DAY_OF_MONTH),
                DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DAY_OF_MONTH), -1)
        );
        
        return aggregateOrdersByBranch(ordersForDate);
    }

    @Override
    public List<BranchSalesDTO> getMonthlySales(int month, int year) {
        List<Order> ordersForMonth = orderRepository.findByOrderDateBetween(
                DateUtils.truncate(new Date(year - 1900, month - 1, 1), Calendar.MONTH),
                DateUtils.addMilliseconds(
                        DateUtils.ceiling(new Date(year - 1900, month - 1, 1), Calendar.MONTH),
                        -1
                )
        );
        
        return aggregateOrdersByBranch(ordersForMonth);
    }

    @Override
    public List<BranchSalesDTO> getYearlySales(int year) {
        List<Order> ordersForYear = orderRepository.findByOrderDateBetween(
                DateUtils.truncate(new Date(year - 1900, 0, 1), Calendar.YEAR),
                DateUtils.addMilliseconds(
                        DateUtils.ceiling(new Date(year - 1900, 0, 1), Calendar.YEAR),
                        -1
                )
        );
        
        return aggregateOrdersByBranch(ordersForYear);
    }

    @Override
    public Double getTotalSales() {
        Double totalSales = orderRepository.getTotalSales();
        return totalSales != null ? totalSales : 0.0;
    }

    @Override
    public Long getTotalOrderCount() {
        return orderRepository.count();
    }

    @Override
    public List<DailySalesSummaryDTO> getDailySalesByBranch(Long branchId) {
        List<Order> orders = orderRepository.findAllByBranchId(branchId);
        
        Map<LocalDate, Double> dailySalesMap = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.summingDouble(Order::getTotal)
                ));
        
        Map<LocalDate, Long> dailyOrdersCountMap = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.counting()
                ));
        
        return dailySalesMap.entrySet().stream()
                .map(entry -> new DailySalesSummaryDTO(
                        entry.getKey(),
                        dailyOrdersCountMap.getOrDefault(entry.getKey(), 0L),
                        entry.getValue()
                ))
                .sorted(Comparator.comparing(DailySalesSummaryDTO::getDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<BranchDailySalesSummaryDTO> getAllDailySales() {
        List<Order> orders = orderRepository.findAll();
        
        Map<Long, Map<LocalDate, List<Order>>> branchDateOrderMap = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getBranchId,
                        Collectors.groupingBy(
                                order -> order.getOrderDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        )
                ));
        
        List<BranchDailySalesSummaryDTO> result = new ArrayList<>();
        
        for (Map.Entry<Long, Map<LocalDate, List<Order>>> branchEntry : branchDateOrderMap.entrySet()) {
            long branchId = branchEntry.getKey();
            Map<LocalDate, List<Order>> dateOrderMap = branchEntry.getValue();
            
            List<DailySalesSummaryDTO> dailySalesSummaries = dateOrderMap.entrySet().stream()
                    .map(dateEntry -> {
                        LocalDate date = dateEntry.getKey();
                        List<Order> dailyOrders = dateEntry.getValue();
                        double totalSales = dailyOrders.stream().mapToDouble(Order::getTotal).sum();
                        long orderCount = dailyOrders.size();
                        return new DailySalesSummaryDTO(date, orderCount, totalSales);
                    })
                    .sorted(Comparator.comparing(DailySalesSummaryDTO::getDate))
                    .collect(Collectors.toList());
            
            result.add(new BranchDailySalesSummaryDTO(branchId, dailySalesSummaries));
        }
        
        return result;
    }

    /**
     * Helper method to aggregate orders by branch
     */
    private List<BranchSalesDTO> aggregateOrdersByBranch(List<Order> orders) {
        Map<Long, Double> branchSalesMap = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getBranchId,
                        Collectors.summingDouble(Order::getTotal)
                ));
        
        Map<Long, Long> branchOrdersCountMap = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getBranchId,
                        Collectors.counting()
                ));
        
        return branchSalesMap.entrySet().stream()
                .map(entry -> BranchSalesDTO.builder()
                        .branchId(entry.getKey())
                        .totalSales(entry.getValue())
                        .orderCount(branchOrdersCountMap.getOrDefault(entry.getKey(), 0L))
                        .build())
                .collect(Collectors.toList());
    }
}
