package com.himanshu.departmentalStore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a product entity in the departmental store system.
 * <p>This entity stores information about products including their name, description, price, expiry date, availability, and count in stock.
 */
@Entity
@Data
public class Product {

    /**
     * The unique identifier for the product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The description of the product.
     */
    private String description;

    /**
     * The price of the product.
     */
    private BigDecimal price;

    /**
     * The expiry date of the product.
     */
    private LocalDate expiry;

    /**
     * The count of the product in stock.
     */
    private int count;

    /**
     * The availability status of the product.
     */
    private boolean availability;
}
