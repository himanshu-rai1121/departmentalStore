package com.himanshu.departmentalStore.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Exception thrown when a requested resource is not found.
 */
@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    /**
     * The name of the resource that was not found.
     */
    private final String execptionResourceName;

    /**
     * The name of the field used to search for the resource.
     */
    private final String execptionFieldName;

    /**
     * The value of the field used to search for the resource.
     */
    private final long execptionFieldValue;

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * @param resourceName The name of the resource that was not found.
     * @param fieldName The name of the field used to search for the resource.
     * @param fieldValue The value of the field used to search for the resource.
     */
    public ResourceNotFoundException(final String resourceName, final String fieldName, final long fieldValue) {
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        this.execptionResourceName = resourceName;
        this.execptionFieldName = fieldName;
        this.execptionFieldValue = fieldValue;
    }

}
