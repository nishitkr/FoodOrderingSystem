package com.nishit.FoodOrdering.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class ResponseDTO<T> implements Serializable {

    @JsonProperty("data")
    private T data;

    @JsonProperty("error")
    private ErrorDetails errorDetails;
}




