package com.nishit.FoodOrdering.controller;

import com.nishit.FoodOrdering.dto.MessageDTO;
import com.nishit.FoodOrdering.dto.request.ReviewFoodItemRequestDTO;
import com.nishit.FoodOrdering.dto.request.ReviewRestaurantRegistrationRequestDTO;
import com.nishit.FoodOrdering.dto.response.ErrorDetails;
import com.nishit.FoodOrdering.dto.response.impl.MessageResponseDTO;
import com.nishit.FoodOrdering.exception.DataNotFoundException;
import com.nishit.FoodOrdering.exception.OperationNotAllowedException;
import com.nishit.FoodOrdering.service.FoodItemService;
import com.nishit.FoodOrdering.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private FoodItemService foodItemService;

    @Autowired
    private RestaurantService restaurantService;

    @Operation(summary = "Review food item addition request")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping(value = "/reviewFoodItem", produces = "application/json")
    public ResponseEntity<MessageResponseDTO> reviewAddFoodItemRequest(@RequestBody ReviewFoodItemRequestDTO reviewFoodItemRequestDTO) {
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO();
        try {
            foodItemService.handleFoodItemRequestReview(reviewFoodItemRequestDTO);
            messageResponseDTO.setData(MessageDTO.builder()
                    .message("Review saved successfully")
                    .build());
            messageResponseDTO.setErrorDetails(null);
            return ResponseEntity.ok(messageResponseDTO);
        } catch (OperationNotAllowedException | DataNotFoundException ex) {
            messageResponseDTO.setData(null);
            messageResponseDTO.setErrorDetails(ErrorDetails.builder()
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponseDTO);
        } catch (Exception ex) {
            messageResponseDTO.setData(null);
            messageResponseDTO.setErrorDetails(ErrorDetails.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponseDTO);
        }
    }

    @Operation(summary = "Review restaurant registration request")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping(value = "/reviewRestaurant", produces = "application/json")
    public ResponseEntity<MessageResponseDTO> reviewRestaurantRegistration(
            @RequestBody ReviewRestaurantRegistrationRequestDTO reviewRestaurantRegistrationRequestDTO) {
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO();
        try {
            restaurantService.handleRestaurantRegistrationRequestReview(reviewRestaurantRegistrationRequestDTO);
            messageResponseDTO.setData(MessageDTO.builder()
                            .message("Review saved successfully")
                    .build());
            messageResponseDTO.setErrorDetails(null);
            return ResponseEntity.ok(messageResponseDTO);
        } catch (OperationNotAllowedException | DataNotFoundException ex) {
            messageResponseDTO.setData(null);
            messageResponseDTO.setErrorDetails(ErrorDetails.builder()
                            .errorCode(HttpStatus.BAD_REQUEST.name())
                            .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponseDTO);
        } catch (Exception ex) {
            messageResponseDTO.setData(null);
            messageResponseDTO.setErrorDetails(ErrorDetails.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponseDTO);
        }
    }
}
