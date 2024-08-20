package com.nishit.FoodOrdering.controller;

import com.nishit.FoodOrdering.dto.request.CreateMenuDTO;
import com.nishit.FoodOrdering.exception.DataNotFoundException;
import com.nishit.FoodOrdering.exception.OperationNotAllowedException;
import com.nishit.FoodOrdering.exception.ServiceException;
import com.nishit.FoodOrdering.dto.response.ErrorDetails;
import com.nishit.FoodOrdering.dto.request.RegisterRestaurantDTO;
import com.nishit.FoodOrdering.dto.RestaurantDTO;
import com.nishit.FoodOrdering.dto.response.impl.RestaurantResponseDTO;
import com.nishit.FoodOrdering.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;


    @Operation(summary = "Submits restaurant registration request into the food ordering system")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<RestaurantResponseDTO> registerRestaurant(@Valid @RequestBody RegisterRestaurantDTO registerRestaurantDTO) {
        RestaurantResponseDTO restaurantResponseDTO;
        try {
            RestaurantDTO restaurantDTO = restaurantService.handleRestaurantRegistration(registerRestaurantDTO);
            restaurantResponseDTO = new RestaurantResponseDTO();
            restaurantResponseDTO.setData(restaurantDTO);
            restaurantResponseDTO.setErrorDetails(null);
            return ResponseEntity.ok(restaurantResponseDTO);
        } catch (Exception ex) {
            restaurantResponseDTO = new RestaurantResponseDTO();
            restaurantResponseDTO.setData(null);
            restaurantResponseDTO.setErrorDetails(ErrorDetails.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(restaurantResponseDTO);
        }
    }

    @Operation(summary = "Create menu for a restaurant")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping(value = "/createMenu", produces = "application/json")
    public ResponseEntity<RestaurantResponseDTO> createRestaurantMenu(@RequestBody CreateMenuDTO createMenuDTO) {
        RestaurantResponseDTO restaurantResponseDTO = new RestaurantResponseDTO();
        try {
            RestaurantDTO restaurantDTO = restaurantService.handleMenuCreation(createMenuDTO);
            restaurantResponseDTO.setData(restaurantDTO);
            restaurantResponseDTO.setErrorDetails(null);
            return ResponseEntity.ok(restaurantResponseDTO);
        } catch (DataNotFoundException | OperationNotAllowedException ex) {
            restaurantResponseDTO.setData(null);
            restaurantResponseDTO.setErrorDetails(ErrorDetails.builder()
                            .errorCode(HttpStatus.BAD_REQUEST.name())
                            .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restaurantResponseDTO);
        } catch (Exception ex) {
            restaurantResponseDTO.setData(null);
            restaurantResponseDTO.setErrorDetails(ErrorDetails.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(restaurantResponseDTO);
        }
    }

    @Operation(summary = "Get details of a restaurant along with list of menu items")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @GetMapping(value = "/getDetails", produces = "application/json")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantDetails(@RequestParam(name = "restaurant_id") String restaurantId) {
        RestaurantResponseDTO restaurantResponseDTO = new RestaurantResponseDTO();

        try {
            RestaurantDTO restaurantDTO = restaurantService.getRestaurantDetails(restaurantId);
            restaurantResponseDTO.setData(restaurantDTO);
            restaurantResponseDTO.setErrorDetails(null);
            return ResponseEntity.ok(restaurantResponseDTO);
        } catch (DataNotFoundException ex) {
            restaurantResponseDTO.setData(null);
            restaurantResponseDTO.setErrorDetails(ErrorDetails.builder()
                            .errorCode(HttpStatus.BAD_REQUEST.name())
                            .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restaurantResponseDTO);
        } catch (Exception ex) {
            restaurantResponseDTO.setData(null);
            restaurantResponseDTO.setErrorDetails(ErrorDetails.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(restaurantResponseDTO);
        }
    }
}
