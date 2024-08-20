package com.nishit.FoodOrdering.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
