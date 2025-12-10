package com.lifepill.prescription.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for order placement.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPlacementResponse {
    
    private UUID orderId;
    private UUID prescriptionId;
    private Long branchId;
    private String branchName;
    private UUID userId;
    private String status;
    private BigDecimal totalAmount;
    private String deliveryAddress;
    private String paymentMethod;
    private LocalDateTime orderPlacedAt;
    private String estimatedDeliveryTime;
    private List<OrderItem> items;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItem {
        private String medicineName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }
}
