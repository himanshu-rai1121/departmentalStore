package com.himanshu.departmentalStore.exception;

import lombok.Getter;
import lombok.Setter;
import java.util.Optional;
@Getter
@Setter
public class CustomException  extends RuntimeException{
    private final String message;
    private final Optional<Object> body;
    public CustomException(final String message, final Object body) {
        super(message);
        this.message = message;
        this.body = Optional.ofNullable(body);
    }
}