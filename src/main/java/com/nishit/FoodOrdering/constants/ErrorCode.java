package com.nishit.FoodOrdering.constants;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    BAD_REQUEST(400, "Bad Request"),

    DATA_NOT_FOUND_EXCEPTION(4004,"Data not found in the system"),
    OPERATION_NOT_ALLOWED_EXCEPTION(4005,"Operation not allowed in the system"),

    DB_SAVE_EXCEPTION(5001,"Exception while saving the data in database");

    private final Integer code;
    private final String description;

    ErrorCode(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
