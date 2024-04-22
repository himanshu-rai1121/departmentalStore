package com.himanshu.departmentalStore.exception;

import lombok.Getter;
import lombok.Setter;
import java.util.Optional;

/**
 * Custom exception class for handling application-specific exceptions.
 * This exception extends the RuntimeException class.
 */
@Getter
@Setter
public class CustomException  extends RuntimeException {
    /**
     * Message associated with the exception.
     */
    private final String message;

    /**
     * Optional body associated with the exception.
     * This can contain additional information related to the exception.
     */
    private final Optional<Object> body;

    /**
     * Constructs a new CustomException with the specified message and body.
     * @param message The detail message (which is saved for later retrieval by the getMessage() method)
     * @param body    Optional additional information related to the exception
     */
    public CustomException(final String message, final Object body) {
        super(message);
        this.message = message;
        this.body = Optional.ofNullable(body);
    }
}