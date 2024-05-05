package com.himanshu.departmentalStore.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents an API response containing a message and status.
 */
@Getter
@AllArgsConstructor
public class ApiResponse {

    /**
     * The message included in the API response.
     */
    private String message;

    /**
     * The status indicating the success or failure of the API operation.
     */
    private boolean status;

    /**
     * The body include the response body.
     */
    private Object body;
}
