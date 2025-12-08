package com.lifepill.possystem.service;

import com.lifepill.possystem.dto.requestDTO.RequestOrderSaveDTO;
import com.lifepill.possystem.entity.Order;

/**
 * Service interface for sending emails related to orders.
 */
public interface EmailService {
    
    /**
     * Sends an order confirmation email to the customer.
     *
     * @param recipientEmail The email address of the recipient
     * @param customerName   The name of the customer
     * @param order          The order entity with generated ID and dates
     * @param orderDTO       The order DTO with complete details (items, payment, etc.)
     */
    void sendOrderConfirmationEmail(String recipientEmail, String customerName, Order order, RequestOrderSaveDTO orderDTO);
}
