package com.himanshu.departmentalStore.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

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
    private final String exceptionMessage;

    /**
     * Optional body associated with the exception.
     * This can contain additional information related to the exception.
     */
    private final Optional<Object> exceptionBody;
    private final HttpStatus exceptionHttpStatus;

    /**
     * Constructs a new CustomException with the specified message and body.
     * @param message The detail message (which is saved for later retrieval by the getMessage() method)
     * @param body    Optional additional information related to the exception
     */
    public CustomException(final String message, final Object body, final HttpStatus httpStatus) {
        super(message);
        this.exceptionMessage = message;
        this.exceptionBody = Optional.ofNullable(body);
        this.exceptionHttpStatus = httpStatus;
    }
}
