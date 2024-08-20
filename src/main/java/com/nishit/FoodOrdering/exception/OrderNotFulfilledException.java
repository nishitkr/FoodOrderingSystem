package com.nishit.FoodOrdering.exception;

public class OrderNotFulfilledException extends RuntimeException {
    public OrderNotFulfilledException(String message) {
        super(message);
    }

    public OrderNotFulfilledException(String message, Throwable cause) {
        super(message, cause);
    }
}
