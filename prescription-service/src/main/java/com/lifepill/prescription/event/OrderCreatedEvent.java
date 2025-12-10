package com.lifepill.prescription.event;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Event DTO published when an order is created from a prescription.
 * This event is consumed by the Order Service and Notification Service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreatedEvent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private UUID orderId;
    private UUID prescriptionId;
    private UUID userId;
    private UUID branchId;
    private UUID responseId;
    private BigDecimal totalAmount;
    private String deliveryAddress;
    private String deliveryNotes;
    private String paymentMethod;
    private String contactPhone;
    private LocalDateTime orderPlacedAt;
    private List<OrderItemInfo> items;
    
    @Builder.Default
    private String eventType = "ORDER_CREATED";
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String medicineName;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
