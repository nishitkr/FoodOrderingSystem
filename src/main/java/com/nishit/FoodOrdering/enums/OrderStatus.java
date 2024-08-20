package com.nishit.FoodOrdering.enums;

public enum OrderStatus {
    PROCESSING("Processing"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    DISPATCHED("Dispatched"),
    DELIVERED("Delivered");

    public final String statusValue;


    OrderStatus(String statusValue) {
        this.statusValue = statusValue;
    }

    public String value() {
        return statusValue;
    }
}
