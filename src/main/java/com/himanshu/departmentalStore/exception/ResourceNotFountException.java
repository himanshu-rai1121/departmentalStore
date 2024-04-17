package com.himanshu.departmentalStore.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Exception thrown when a requested resource is not found.
 */
@Getter
@Setter
public class ResourceNotFountException extends RuntimeException {

    /**
     * The name of the resource that was not found.
     */
    private final String resourceName;

    /**
     * The name of the field used to search for the resource.
     */
    private final String fieldName;

    /**
     * The value of the field used to search for the resource.
     */
    private final long fieldValue;

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * @param resourceName The name of the resource that was not found.
     * @param fieldName The name of the field used to search for the resource.
     * @param fieldValue The value of the field used to search for the resource.
     */
    public ResourceNotFountException(final String resourceName, final String fieldName, final long fieldValue) {
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
