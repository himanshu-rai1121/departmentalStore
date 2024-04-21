package com.himanshu.departmentalStore.model;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an order entity in the departmental store system.
 * <p>This entity stores information about orders.
 * It includes the product, customer, timestamp, quantity, and associated discount.
 */
@Entity
@Getter
@Setter
@Table(name = "customer_order")
public class Order {

    /**
     * The unique identifier for the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The product associated with the order.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * The customer who placed the order.
     */
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    /**
     * The timestamp indicating when the order was placed.
     */
    private LocalDateTime timestamp;

    /**
     * The quantity of the product ordered.
     */
    private int quantity;

    /**
     * The discount associated with the order (if any).
     * optional so that we can set discount to null if not applicable.
     */
    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    /**
     * The total price associated with the order.
     * After adding discount if applicable.
     */
    private BigDecimal amount;
}
