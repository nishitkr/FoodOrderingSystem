package com.nishit.FoodOrdering.enums;

public enum ApprovalStatus {
    SUBMITTED("Submitted"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    public final String statusValue;


    ApprovalStatus(String statusValue) {
        this.statusValue = statusValue;
    }

    public String value() {
        return statusValue;
    }
}
