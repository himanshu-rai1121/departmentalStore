package com.himanshu.departmentalStore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Represents a Customer entity in the departmental store system.
 * This entity stores information about customers including their name, address, and contact number.
 */

@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "name can not be null")
    private String fullName;
    @NotNull
    private String address;
    @NotNull
    private String contactNumber;
}
