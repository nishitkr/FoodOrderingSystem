package com.nishit.FoodOrdering.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterUserDTO {

    @NotBlank(message = "Name must not be empty or null.")
    @Size(max = 50, message = "The length of name must be less than 50.")
    @JsonProperty("name")
    private String name;

    @NotEmpty(message = "Email must not be empty or null.")
    @Email(message = "The email address is invalid.")
    @Size(max = 320, message = "The length of email must be less than 320.")
    @JsonProperty("email")
    private String email;

    @NotBlank
    @Size(max = 25, message = "The length of phone must be less than 25.")
    @JsonProperty("phone")
    private String phone;

    @NotBlank(message = "Address must not be empty or null.")
    @Size(max = 1000, message = "The length of address must be less than 1000.")
    @JsonProperty("address")
    private String address;
}
