package com.himanshu.departmentalStore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for handling exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException and returns an appropriate API response with HTTP status 404 Not Found.
     * @param ex The ResourceNotFoundException thrown
     * @return ResponseEntity containing an ApiResponse with the error message and status
     */
    @ExceptionHandler(ResourceNotFountException.class)
    public ResponseEntity<ApiResponse> resourceNotFountExceptionHandler(ResourceNotFountException ex) {
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles general exceptions and returns an appropriate API response with HTTP status 500 Internal Server Error.
     * @param ex The exception thrown
     * @return ResponseEntity containing an ApiResponse with the error message and status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> globalException(Exception ex) {
        String message = "Some Error Occurred: " + ex.getCause();
        ApiResponse apiResponse = new ApiResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
