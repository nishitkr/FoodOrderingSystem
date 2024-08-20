package com.nishit.FoodOrdering.controller;

import com.nishit.FoodOrdering.dto.AddFoodItemDTO;
import com.nishit.FoodOrdering.dto.AddFoodItemRequestsByRestaurantListDTO;
import com.nishit.FoodOrdering.dto.FoodItemDTO;
import com.nishit.FoodOrdering.dto.response.ErrorDetails;
import com.nishit.FoodOrdering.dto.request.AddFoodItemRequestDTO;
import com.nishit.FoodOrdering.dto.response.impl.AddFoodItemResponseDTO;
import com.nishit.FoodOrdering.dto.response.impl.AddFoodItemResponseListDTO;
import com.nishit.FoodOrdering.dto.response.impl.FoodItemListResponseDTO;
import com.nishit.FoodOrdering.exception.DataNotFoundException;
import com.nishit.FoodOrdering.exception.InvalidDataException;
import com.nishit.FoodOrdering.exception.OperationNotAllowedException;
import com.nishit.FoodOrdering.service.FoodItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/foodItem")
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;


    @Operation(summary = "Request for adding a food item on the food ordering platform")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity<AddFoodItemResponseDTO> requestFoodItemAddition(@Valid @RequestBody AddFoodItemRequestDTO addFoodItemRequestDTO) {
        AddFoodItemResponseDTO addFoodItemResponseDTO = new AddFoodItemResponseDTO();
        try {
            AddFoodItemDTO addFoodItemDTO = foodItemService.handleAddFoodItemAdditionRequest(addFoodItemRequestDTO);
            addFoodItemResponseDTO.setData(addFoodItemDTO);
            addFoodItemResponseDTO.setErrorDetails(null);
            return ResponseEntity.ok(addFoodItemResponseDTO);
        } catch (InvalidDataException | OperationNotAllowedException | DataNotFoundException ex) {
            addFoodItemResponseDTO.setData(null);
            addFoodItemResponseDTO.setErrorDetails(ErrorDetails.builder()
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .description(ex.getMessage())
                    .build());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(addFoodItemResponseDTO);
        } catch (Exception ex) {
            addFoodItemResponseDTO.setData(null);
            addFoodItemResponseDTO.setErrorDetails(ErrorDetails.builder()
                            .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                            .description(ex.getMessage())
                    .build());
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(addFoodItemResponseDTO);
        }
    }

    @Operation(summary = "List food item addition requests by a restaurant")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @GetMapping(value = "/listRestaurantRequests", produces = "application/json")
    public ResponseEntity<AddFoodItemResponseListDTO> listAddFoodItemRequestsByRestaurant(
            @Valid @NotBlank @RequestParam(name = "restaurant_id") String restaurantId) {
        AddFoodItemResponseListDTO addFoodItemResponseListDTO = new AddFoodItemResponseListDTO();

        try {
            AddFoodItemRequestsByRestaurantListDTO addFoodItemRequestsByRestaurantListDTO
                    = foodItemService.listAddFoodItemRequestsByRestaurant(restaurantId);
            addFoodItemResponseListDTO.setData(addFoodItemRequestsByRestaurantListDTO);
            addFoodItemResponseListDTO.setErrorDetails(null);
            return ResponseEntity.ok(addFoodItemResponseListDTO);
        } catch (DataNotFoundException ex) {
            addFoodItemResponseListDTO.setData(null);
            addFoodItemResponseListDTO.setErrorDetails(ErrorDetails.builder()
                            .errorCode(HttpStatus.NOT_FOUND.name())
                            .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(addFoodItemResponseListDTO);
        } catch (Exception ex) {
            addFoodItemResponseListDTO.setData(null);
            addFoodItemResponseListDTO.setErrorDetails(ErrorDetails.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(addFoodItemResponseListDTO);
        }
    }

    @Operation(summary = "Get full catalog of food items available on the food ordering platform")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @GetMapping(value = "/list", produces = "application/json")
    public ResponseEntity<FoodItemListResponseDTO> getListOfFoodItems() {
        FoodItemListResponseDTO foodItemListResponseDTO = new FoodItemListResponseDTO();
        try {
            List<FoodItemDTO> availableFoodItemsList = foodItemService.fetchFoodItemCatalog();
            foodItemListResponseDTO.setData(availableFoodItemsList);
            foodItemListResponseDTO.setErrorDetails(null);
            return ResponseEntity.ok(foodItemListResponseDTO);
        } catch (Exception ex) {
            foodItemListResponseDTO.setData(null);
            foodItemListResponseDTO.setErrorDetails(ErrorDetails.builder()
                            .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                            .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(foodItemListResponseDTO);
        }
    }
}
