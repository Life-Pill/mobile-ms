package com.lifepill.possystem.entity;

import lombok.*;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Order entity - represents a customer order
 * Uses employerId to reference employee from Identity Service (no direct entity dependency)
 */
@Entity
@Table(name = "orders")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @Column(name = "order_id", length = 45)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    /**
     * Reference to employee from Identity Service
     * No direct entity relationship - uses Feign client for data retrieval
     */
    @Column(name = "employer_id", nullable = false)
    private Long employerId;

    /**
     * Reference to branch from Branch Service
     * No direct entity relationship - uses Feign client for data retrieval
     */
    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Column(name = "order_date", columnDefinition = "TIMESTAMP")
    private Date orderDate;

    @Column(name = "total", nullable = false)
    private Double total;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderDetails> orderDetails;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PaymentDetails> paymentDetails;

    /**
     * Instantiates a new Order with basic details.
     *
     * @param employerId the employer ID (from Identity Service)
     * @param branchId   the branch ID (from Branch Service)
     * @param orderDate  the order date
     * @param total      the total amount
     */
    public Order(Long employerId, Long branchId, Date orderDate, Double total) {
        this.employerId = employerId;
        this.branchId = branchId;
        this.orderDate = orderDate;
        this.total = total;
    }
}
