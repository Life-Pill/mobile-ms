package com.lifepill.possystem.service;

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
     * @param order          The order details to include in the email
     */
    void sendOrderConfirmationEmail(String recipientEmail, String customerName, Order order);
}
