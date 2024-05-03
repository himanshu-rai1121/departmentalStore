package com.himanshu.departmentalStore.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
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
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
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
    @NotNull(message = "Product_id can not be null")
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * The customer who placed the backorder.
     */
    @NotNull(message = "Customer_Id can not be null")
    @ManyToOne(optional = false)
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
