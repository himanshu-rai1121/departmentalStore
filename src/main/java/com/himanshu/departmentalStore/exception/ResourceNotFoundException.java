package com.himanshu.departmentalStore.exception;

import lombok.Setter;

/**
 * Exception thrown when a requested resource is not found.
 */
@Setter
public class ResourceNotFoundException extends RuntimeException {

    /**
     * The name of the resource that was not found.
     */
    private final String excptionResourceName;

    /**
     * The name of the field used to search for the resource.
     */
    private final String exceptionFieldName;

    /**
     * The value of the field used to search for the resource.
     */
    private final long excptionFieldValue;

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * @param resourceName The name of the resource that was not found.
     * @param fieldName The name of the field used to search for the resource.
     * @param fieldValue The value of the field used to search for the resource.
     */
    public ResourceNotFoundException(final String resourceName, final String fieldName, final long fieldValue) {
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        this.excptionResourceName = resourceName;
        this.exceptionFieldName = fieldName;
        this.excptionFieldValue = fieldValue;
    }

}
