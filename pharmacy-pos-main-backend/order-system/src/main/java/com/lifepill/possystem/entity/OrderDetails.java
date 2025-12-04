package com.lifepill.possystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * The type Order details.
 */
@Entity
@Table(name = "order_details")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetails {
    @Id
    @Column(name = "order_details_id",length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderDetailsId;

    @Column(name = "name",length = 100,nullable = false)
    private String name;

    @Column(name = "amount",length = 100,nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order orders;

    // Reference to item in Inventory Service (no FK constraint)
    @Column(name = "item_id", nullable = false)
    private Long itemId;

}