package com.nishit.FoodOrdering.controller;

import com.nishit.FoodOrdering.exception.ServiceException;
import com.nishit.FoodOrdering.dto.response.ErrorDetails;
import com.nishit.FoodOrdering.dto.UserDTO;
import com.nishit.FoodOrdering.dto.request.RegisterUserDTO;
import com.nishit.FoodOrdering.dto.response.impl.UserResponseDTO;
import com.nishit.FoodOrdering.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Register a user into the food ordering system")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        UserResponseDTO userResponseDTO;
        try {
            UserDTO restaurantDTO = userService.handleUserRegistration(registerUserDTO);
            userResponseDTO = new UserResponseDTO();
            userResponseDTO.setData(restaurantDTO);
            userResponseDTO.setErrorDetails(null);
            return ResponseEntity.ok(userResponseDTO);
        } catch (Exception ex) {
            userResponseDTO = new UserResponseDTO();
            userResponseDTO.setData(null);
            userResponseDTO.setErrorDetails(ErrorDetails.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponseDTO);
        }
    }
}
