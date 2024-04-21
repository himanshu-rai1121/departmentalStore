package com.himanshu.departmentalStore.model;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a backorder entity,
 * which indicates an order for a product that cannot be immediately fulfilled.
 */
@Entity
@Getter
@Setter
public class Backorder {

    /**
     * The unique identifier for the backorder.
     * primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The product associated with the backorder.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * The customer who placed the backorder.
     */
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    /**
     * The timestamp indicating when the backorder was placed.
     */
    private LocalDateTime timestamp;

    /**
     * The quantity of the product for which the backorder is placed.
     */
    private int quantity;

}
