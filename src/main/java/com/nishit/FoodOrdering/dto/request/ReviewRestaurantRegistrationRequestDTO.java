package com.nishit.FoodOrdering.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewRestaurantRegistrationRequestDTO {
    @NotBlank(message = "ID must not be empty or null.")
    @Size(max = 50, message = "The length of ID must be less than 50.")
    @JsonProperty("id")
    private String id;

    @NotNull
    @JsonProperty("approved")
    private boolean approved;

    @NotBlank(message = "Approver comment must not be empty or null.")
    @Size(max = 255, message = "The length of approver comment must be less than 255.")
    @JsonProperty("approver_comment")
    private String approverComment;
}
