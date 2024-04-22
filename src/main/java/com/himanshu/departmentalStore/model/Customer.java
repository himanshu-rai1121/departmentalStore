package com.himanshu.departmentalStore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Customer entity in the departmental store system.
 * This entity stores information about customers including their name, address, and contact number.
 */

@Entity
@Getter
@Setter
public class Customer {

    /**
     * The unique identifier for the customer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The full name of the customer.
     * Cannot be null.
     */
    @NotNull(message = "Name can not be null")
    private String fullName;

    /**
     * The address of the customer.
     * Cannot be null.
     */
    @NotNull(message = "Address can not be null")
    private String address;

    /**
     * The contact number of the customer.
     * Cannot be null.
     */
    @NotNull(message = "Contact Number can not be null")
    private String contactNumber;
}
