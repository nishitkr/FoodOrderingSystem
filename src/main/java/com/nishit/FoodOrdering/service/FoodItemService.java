package com.nishit.FoodOrdering.service;

import com.nishit.FoodOrdering.constants.ErrorCode;
import com.nishit.FoodOrdering.dto.AddFoodItemDTO;
import com.nishit.FoodOrdering.dto.AddFoodItemRequestsByRestaurantListDTO;
import com.nishit.FoodOrdering.dto.FoodItemDTO;
import com.nishit.FoodOrdering.dto.RestaurantDTO;
import com.nishit.FoodOrdering.dto.request.AddFoodItemRequestDTO;
import com.nishit.FoodOrdering.dto.request.ReviewFoodItemRequestDTO;
import com.nishit.FoodOrdering.entity.AddFoodItemEntity;
import com.nishit.FoodOrdering.entity.FoodItemEntity;
import com.nishit.FoodOrdering.enums.ApprovalStatus;
import com.nishit.FoodOrdering.enums.AvailabilityStatus;
import com.nishit.FoodOrdering.exception.DataNotFoundException;
import com.nishit.FoodOrdering.exception.InvalidDataException;
import com.nishit.FoodOrdering.exception.OperationNotAllowedException;
import com.nishit.FoodOrdering.exception.ServiceException;
import com.nishit.FoodOrdering.repository.AddFoodItemRepository;
import com.nishit.FoodOrdering.repository.FoodItemRepository;
import com.nishit.FoodOrdering.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FoodItemService {

    @Autowired private AddFoodItemRepository addFoodItemRepository;
    @Autowired private FoodItemRepository foodItemRepository;
    @Autowired private RestaurantService restaurantService;

    @Autowired private Validator<AddFoodItemRequestDTO> addFoodItemRequestValidator;

    public List<FoodItemDTO> fetchFoodItemCatalog() {
        List<FoodItemEntity> availableFoodItemsList = foodItemRepository.findAllAvailableItemsWithAtLeastOneServingRestaurant();
        return availableFoodItemsList.stream()
                .map(FoodItemEntity::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public AddFoodItemDTO handleAddFoodItemAdditionRequest(AddFoodItemRequestDTO addFoodItemRequestDTO) {
        addFoodItemRequestValidator.validate(addFoodItemRequestDTO);
        AddFoodItemEntity addFoodItemAdditionRequestEntity = addFoodItemRepository.save(AddFoodItemEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .name(addFoodItemRequestDTO.getName())
                        .description(addFoodItemRequestDTO.getDescription())
                        .status(ApprovalStatus.SUBMITTED.value())
                        .restaurantId(addFoodItemRequestDTO.getRestaurantId())
                .build());
        return addFoodItemAdditionRequestEntity.convertEntityToDTO();
    }

    public AddFoodItemRequestsByRestaurantListDTO listAddFoodItemRequestsByRestaurant(String restaurantId) {
        RestaurantDTO restaurantDTO = restaurantService.getRestaurantDetails(restaurantId);
        List<AddFoodItemEntity> addFoodItemEntitiesByRestaurant = addFoodItemRepository.findByRestaurantId(restaurantId);
        return AddFoodItemRequestsByRestaurantListDTO.builder()
                .restaurant(restaurantDTO)
                .addFoodItemDTOList(addFoodItemEntitiesByRestaurant.stream()
                        .map(AddFoodItemEntity::convertEntityToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public void handleFoodItemRequestReview(ReviewFoodItemRequestDTO reviewFoodItemRequestDTO) {
        Optional<AddFoodItemEntity> addFoodItemEntityOptional = addFoodItemRepository.findById(reviewFoodItemRequestDTO.getId());
        if (addFoodItemEntityOptional.isPresent()) {
            AddFoodItemEntity addFoodItemEntityExisting = addFoodItemEntityOptional.get();
            if (ApprovalStatus.SUBMITTED.value().equals(addFoodItemEntityExisting.getStatus())) {
                //update add food item request status and add the item to food_item table
                String approvalStatus = reviewFoodItemRequestDTO.isApproved() ? ApprovalStatus.APPROVED.value() : ApprovalStatus.REJECTED.value();
                addFoodItemEntityExisting.updateApprovalStatusAndComment(approvalStatus, reviewFoodItemRequestDTO.getApproverComment());
                addFoodItemRepository.save(addFoodItemEntityExisting);

                if (reviewFoodItemRequestDTO.isApproved()) {
                    FoodItemEntity foodItemEntityCreated = foodItemRepository.save(FoodItemEntity.builder()
                                    .id(UUID.randomUUID().toString())
                                    .name(addFoodItemEntityExisting.getName())
                                    .description(addFoodItemEntityExisting.getDescription())
                                    .status(AvailabilityStatus.AVAILABLE.value())
                                    .createdOn(Timestamp.from(Instant.now()))
                                    .addFoodItemRequestId(addFoodItemEntityExisting.getId())
                                    .numberOfServingRestaurants(0)
                            .build());
                    log.info("Food item created with id:{}, name:{}", foodItemEntityCreated.getId(), foodItemEntityCreated.getName());
                }
            } else {
                log.error("Food item addition request with id: {} was already reviewed", reviewFoodItemRequestDTO.getId());
                throw new OperationNotAllowedException("Add food item request already reviewed");
            }
        } else {
            log.error("Food item addition request not found with id : {}", reviewFoodItemRequestDTO.getId());
            throw new DataNotFoundException("Add food item request not found");
        }
    }

    public FoodItemEntity getFoodItemById(String foodItemId) {
        Optional<FoodItemEntity> foodItemEntityOptional = foodItemRepository.findById(foodItemId);
        return foodItemEntityOptional.orElse(null);
    }


}
