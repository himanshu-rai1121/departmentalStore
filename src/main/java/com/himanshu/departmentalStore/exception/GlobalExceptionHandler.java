package com.himanshu.departmentalStore.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for handling exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * Handles ResourceNotFoundException and returns an appropriate API response with HTTP status 404 Not Found.
     * @param ex The ResourceNotFoundException thrown
     * @return ResponseEntity containing an ApiResponse with the error message and status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFountExceptionHandler(ResourceNotFoundException ex) {
        logger.error("ResourceNotFoundException: {}", ex.getMessage());
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles general exceptions and returns an appropriate API response with HTTP status 500 Internal Server Error.
     * @param ex The exception thrown
     * @return ResponseEntity containing an ApiResponse with the error message and status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> globalException(Exception ex) {
        logger.error("Global Exception: {}", ex.getMessage());
        String message = "Some Error Occurred: " + ex.getCause();
        ApiResponse apiResponse = new ApiResponse(message, false, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles CustomException and returns an appropriate API response with HTTP status 404 Not Found.
     * Creates an ApiResponse containing the message and optional body from CustomException.
     * @param ex The CustomException thrown
     * @return ResponseEntity containing an ApiResponse with the error message and status
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> customException(Exception ex) {
        logger.error("Custom Exception: {}", ex.getMessage());
        CustomException customException = (CustomException) ex;
        String message = ex.getMessage();
        Object body = customException.getBody().orElse(null); // Get the body from CustomException
        ApiResponse apiResponse = new ApiResponse(message, false, body);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
}
