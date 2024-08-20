package com.nishit.FoodOrdering.enums;

public enum AvailabilityStatus {
    AVAILABLE("Available"),
    UNAVAILABLE("Unavailable");

    public final String statusValue;

    AvailabilityStatus(String statusValue) {
        this.statusValue = statusValue;
    }

    public String value() {
        return statusValue;
    }
}
