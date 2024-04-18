package com.himanshu.departmentalStore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a discount entity in the departmental store system.
 * This entity stores information about discounts including their name, value, duration, and conditions.
 */
@Entity
@Data
public class Discount {

    /**
     * The unique identifier for the discount.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the discount.
     */
    private String name;

    /**
     * The value of the discount.
     * (in percentage)
     */
    private BigDecimal value;

    /**
     * The start date of the discount.
     */
    private LocalDateTime startDateTime;

    /**
     * The end date of the discount.
     */
    private LocalDateTime endDateTime;

    /**
     * The description of the discount.
     */
    private String description;

    /**
     * The minimum price required for the discount to be applicable.
     */
    private BigDecimal minPrice;

    /**
     * The coupon code associated with the discount.
     */
    private String couponCode;
}
