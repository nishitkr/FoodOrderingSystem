package com.nishit.FoodOrdering.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OperationNotAllowedException extends RuntimeException {

    public OperationNotAllowedException(String message) {
        super(message);
    }

    public OperationNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
}
