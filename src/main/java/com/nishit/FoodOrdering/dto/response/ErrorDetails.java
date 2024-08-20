package com.nishit.FoodOrdering.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ErrorDetails {

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("description")
    private String description;
}
