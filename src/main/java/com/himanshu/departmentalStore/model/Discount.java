package com.himanshu.departmentalStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a discount entity in the departmental store system.
 * This entity stores information about discounts including their name, value, duration, and conditions.
 */
@Entity
@Getter
@Setter
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
    @NotNull(message = "Discount name can not be null.")
    private String name;

    /**
     * The value of the discount.
     * (in percentage)
     */
    @NotNull(message = "Value must not be null")
    @Column(name = "discount_value")
    private BigDecimal value;

    /**
     * The start date of the discount.
     */
    @NotNull(message = "Choose the start date time for discount.")
    private LocalDateTime startDateTime;

    /**
     * The end date of the discount.
     */
    @NotNull(message = "Choose the end date time for discount.")
    private LocalDateTime endDateTime;

    /**
     * The description of the discount.
     */
    private String description;

    /**
     * The minimum price required for the discount to be applicable.
     */
    @NotNull(message = "Give minimum price for the discount")
    private BigDecimal minPrice;

    /**
     * The coupon code associated with the discount.
     */
    @NotNull(message = "Provide coupon code for the discount.")
    private String couponCode;
}
