package com.himanshu.departmentalStore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) class representing the request body for creating a backorder.
 * Contains information about the product ID, customer ID, and quantity.
 */
@Getter
@Setter
public class BackOrderRequestBody {

    /**
     * The ID of the product for which the backorder is requested.
     * Cannot be null.
     */
    @NotNull
    private Long productId;

    /**
     * The ID of the customer placing the backorder.
     * Cannot be null.
     */
    @NotNull
    private Long customerId;

    /**
     * The quantity of the product requested in the backorder.
     * Cannot be null.
     */
    @NotNull
    private int quantity;
}

