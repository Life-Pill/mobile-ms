package com.lifepill.prescription.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

/**
 * Request DTO for placing an order from a prescription.
 * User selects a branch response and places an order.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceOrderRequest {
    
    @NotNull(message = "Response ID is required")
    private UUID responseId; // The selected branch response
    
    @NotNull(message = "User ID is required")
    private UUID userId;
    
    private String deliveryAddress;
    
    private String deliveryNotes;
    
    private String paymentMethod; // CASH_ON_DELIVERY, CARD, etc.
    
    private String contactPhone;
}
