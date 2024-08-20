package com.nishit.FoodOrdering.controller;

import com.nishit.FoodOrdering.dto.MessageDTO;
import com.nishit.FoodOrdering.dto.OrderDTO;
import com.nishit.FoodOrdering.dto.OrderItemDispatchDTO;
import com.nishit.FoodOrdering.dto.request.OrderItemDispatchRequestDTO;
import com.nishit.FoodOrdering.dto.request.OrderRatingRequestDTO;
import com.nishit.FoodOrdering.dto.request.OrderRequestDTO;
import com.nishit.FoodOrdering.dto.response.ErrorDetails;
import com.nishit.FoodOrdering.dto.response.impl.MessageResponseDTO;
import com.nishit.FoodOrdering.dto.response.impl.OrderItemDispatchResponseDTO;
import com.nishit.FoodOrdering.dto.response.impl.OrderResponseDTO;
import com.nishit.FoodOrdering.exception.DataNotFoundException;
import com.nishit.FoodOrdering.exception.OperationNotAllowedException;
import com.nishit.FoodOrdering.service.OrderItemRatingService;
import com.nishit.FoodOrdering.service.OrderService;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private OrderItemRatingService orderItemRatingService;

    @Operation(summary = "Place an order")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<OrderResponseDTO> placeOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();

        try {
            OrderDTO orderDTO = orderService.handleOrderCreation(orderRequestDTO);
            orderResponseDTO.setData(orderDTO);
            orderResponseDTO.setErrorDetails(null);
            return ResponseEntity.ok(orderResponseDTO);
        } catch (DataNotFoundException ex) {
            orderResponseDTO.setData(null);
            orderResponseDTO.setErrorDetails(ErrorDetails.builder()
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(orderResponseDTO);
        } catch (Exception ex) {
            orderResponseDTO.setData(null);
            orderResponseDTO.setErrorDetails(ErrorDetails.builder()
                            .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                            .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(orderResponseDTO);
        }
    }

    @Operation(summary = "Mark order items of a restaurant as dispatched")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping(value = "/dispatchOrderItems", produces = "application/json")
    public ResponseEntity<OrderItemDispatchResponseDTO> orderItemDispatch(@Valid @RequestBody OrderItemDispatchRequestDTO orderItemDispatchRequestDTO) {
        OrderItemDispatchResponseDTO orderItemDispatchResponseDTO = new OrderItemDispatchResponseDTO();
        try {
            List<OrderItemDispatchDTO> orderItemDispatchDTOList = orderService.handleOrderItemDispatch(orderItemDispatchRequestDTO);
            orderItemDispatchResponseDTO.setData(orderItemDispatchDTOList);
            orderItemDispatchResponseDTO.setErrorDetails(null);
            return ResponseEntity.ok(orderItemDispatchResponseDTO);
        } catch (DataNotFoundException | OperationNotAllowedException ex) {
            orderItemDispatchResponseDTO.setData(null);
            orderItemDispatchResponseDTO.setErrorDetails(ErrorDetails.builder()
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(orderItemDispatchResponseDTO);
        } catch (Exception ex) {
            orderItemDispatchResponseDTO.setData(null);
            orderItemDispatchResponseDTO.setErrorDetails(ErrorDetails.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .description(ex.getMessage())
                    .build());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(orderItemDispatchResponseDTO);
        }
    }

    @Operation(summary = "Rate order items of an order")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping(value = "/rateOrderItems", produces = "application/json")
    public ResponseEntity<MessageResponseDTO> rateOrderItems(@Valid @RequestBody OrderRatingRequestDTO orderRatingRequestDTO) {
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO();
        try {
            orderItemRatingService.handleOrderItemRating(orderRatingRequestDTO);
            messageResponseDTO.setData(MessageDTO.builder()
                            .message("Submitted ratings. Thanks....!!")
                    .build());
            messageResponseDTO.setErrorDetails(null);
            return ResponseEntity.ok(messageResponseDTO);
        } catch (DataNotFoundException | OperationNotAllowedException ex) {
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
