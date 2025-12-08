package com.lifepill.possystem.service.impl;

import com.lifepill.possystem.dto.requestDTO.RequestOrderDetailsSaveDTO;
import com.lifepill.possystem.dto.requestDTO.RequestOrderSaveDTO;
import com.lifepill.possystem.entity.Order;
import com.lifepill.possystem.entity.OrderDetails;
import com.lifepill.possystem.entity.PaymentDetails;
import com.lifepill.possystem.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Implementation of EmailService for sending order-related emails.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOrderConfirmationEmail(String recipientEmail, String customerName, Order order, RequestOrderSaveDTO orderDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(recipientEmail);
            helper.setSubject("Order Confirmation - Order #" + order.getOrderId());
            helper.setText(buildOrderConfirmationEmail(customerName, order, orderDTO), true);

            mailSender.send(message);
            log.info("Order confirmation email sent to: {}", recipientEmail);
        } catch (MessagingException e) {
            log.error("Failed to send order confirmation email to: {}. Error: {}", recipientEmail, e.getMessage(), e);
            // Don't throw exception - email failure shouldn't block order processing
        }
    }

    private String buildOrderConfirmationEmail(String customerName, Order order, RequestOrderSaveDTO orderDTO) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "LK"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        html.append(".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }");
        html.append(".content { background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; }");
        html.append(".order-info { background-color: white; padding: 15px; margin: 15px 0; border-radius: 5px; }");
        html.append(".order-items { margin: 20px 0; }");
        html.append(".item { padding: 10px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; }");
        html.append(".item:last-child { border-bottom: none; }");
        html.append(".total { font-size: 1.2em; font-weight: bold; color: #4CAF50; margin-top: 15px; text-align: right; }");
        html.append(".footer { text-align: center; margin-top: 20px; color: #666; font-size: 0.9em; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");

        // Header
        html.append("<div class='header'>");
        html.append("<h1>Thank You for Your Order!</h1>");
        html.append("</div>");

        // Content
        html.append("<div class='content'>");
        html.append("<p>Dear ").append(customerName != null ? customerName : "Valued Customer").append(",</p>");
        html.append("<p>Your order has been successfully placed. Here are your order details:</p>");

        // Order Info
        html.append("<div class='order-info'>");
        html.append("<p><strong>Order ID:</strong> #").append(order.getOrderId()).append("</p>");
        html.append("<p><strong>Order Date:</strong> ").append(dateFormat.format(order.getOrderDate())).append("</p>");
        html.append("<p><strong>Branch ID:</strong> ").append(order.getBranchId()).append("</p>");
        html.append("</div>");

        // Order Items
        html.append("<div class='order-items'>");
        html.append("<h3>Items Ordered:</h3>");
        
        if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
            for (OrderDetails item : order.getOrderDetails()) {
                html.append("<div class='item'>");
                html.append("<span>").append(item.getName() != null ? item.getName() : "Item").append("</span>");
                html.append("<span>").append(currencyFormat.format(item.getAmount())).append("</span>");
                html.append("</div>");
            }
        } else {
            html.append("<p>No item details available.</p>");
        }
        html.append("</div>");

        // Payment Details
        if (order.getPaymentDetails() != null && !order.getPaymentDetails().isEmpty()) {
            PaymentDetails payment = order.getPaymentDetails().iterator().next();
            html.append("<div class='order-info'>");
            html.append("<h3>Payment Information:</h3>");
            html.append("<p><strong>Payment Method:</strong> ").append(payment.getPaymentMethod()).append("</p>");
            html.append("<p><strong>Payment Amount:</strong> ").append(currencyFormat.format(payment.getPaymentAmount())).append("</p>");
            if (payment.getPaymentDiscount() > 0) {
                html.append("<p><strong>Discount:</strong> ").append(currencyFormat.format(payment.getPaymentDiscount())).append("</p>");
            }
            html.append("<p><strong>Paid Amount:</strong> ").append(currencyFormat.format(payment.getPaidAmount())).append("</p>");
            if (payment.getPaymentNotes() != null && !payment.getPaymentNotes().isEmpty()) {
                html.append("<p><strong>Notes:</strong> ").append(payment.getPaymentNotes()).append("</p>");
            }
            html.append("</div>");
        }

        // Total
        html.append("<div class='total'>");
        html.append("Total: ").append(currencyFormat.format(order.getTotal()));
        html.append("</div>");

        // Footer
        html.append("<div class='footer'>");
        html.append("<p>Thank you for choosing LifePill Pharmacy!</p>");
        html.append("<p>If you have any questions, please don't hesitate to contact us.</p>");
        html.append("</div>");

        html.append("</div>"); // content
        html.append("</div>"); // container
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}
