package com.himanshu.departmentalStore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO (Data Transfer Object) class representing the request body for creating an order.
 * Contains information about the product ID, customer ID, quantity, and discount ID.
 */
@Data
public class OrderRequestBody {

    /**
     * The ID of the product for which the order is requested.
     * Cannot be null.
     */
    @NotNull
    private Long productId;

    /**
     * The ID of the customer placing the order.
     * Cannot be null.
     */
    @NotNull
    private Long customerId;

    /**
     * The quantity of the product requested in the order.
     * Cannot be null.
     */
    @NotNull
    private int quantity;

    /**
     * The ID of the discount applied to the order.
     */
    private Long discountId;
}

