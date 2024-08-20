package com.nishit.FoodOrdering.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
