package com.nishit.FoodOrdering.service;

import com.nishit.FoodOrdering.dto.request.CreateMenuDTO;
import com.nishit.FoodOrdering.dto.request.ReviewRestaurantRegistrationRequestDTO;
import com.nishit.FoodOrdering.entity.MenuItemEntity;
import com.nishit.FoodOrdering.exception.DataNotFoundException;
import com.nishit.FoodOrdering.exception.OperationNotAllowedException;
import com.nishit.FoodOrdering.exception.ServiceException;
import com.nishit.FoodOrdering.dto.request.RegisterRestaurantDTO;
import com.nishit.FoodOrdering.dto.RestaurantDTO;
import com.nishit.FoodOrdering.entity.RestaurantEntity;
import com.nishit.FoodOrdering.enums.ApprovalStatus;
import com.nishit.FoodOrdering.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuService menuService;

    public RestaurantDTO handleRestaurantRegistration(RegisterRestaurantDTO registerRestaurantDTO) {
        try {
            RestaurantEntity registeredRestaurantEntity = restaurantRepository.save(RestaurantEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .name(registerRestaurantDTO.getName())
                    .tagline(registerRestaurantDTO.getTagline())
                    .processingCapacity(registerRestaurantDTO.getProcessingCapacity())
                    .status(ApprovalStatus.SUBMITTED.value())
                    .rating(0.0)
                    .address(registerRestaurantDTO.getAddress())
                    .build());

            return registeredRestaurantEntity.convertEntityToDTO();
        } catch (Exception ex) {
            log.error("Exception occurred while registering the restaurant : " + registerRestaurantDTO.getName(), ex);
            throw new ServiceException("Exception occurred while registering the restaurant", ex);
        }
    }

    public RestaurantDTO getRestaurantDetails(String restaurantId) {
        Optional<RestaurantEntity> restaurantEntityOptional = restaurantRepository.findById(restaurantId);
        if(restaurantEntityOptional.isPresent()) {
            return restaurantEntityOptional.get().convertEntityToDTO();
        } else {
            log.error("Restaurant not found with id : {}", restaurantId);
            throw new DataNotFoundException("Restaurant not found");
        }
    }

    public void handleRestaurantRegistrationRequestReview(ReviewRestaurantRegistrationRequestDTO reviewRestaurantRegistrationRequestDTO) {
        Optional<RestaurantEntity> restaurantEntityOptional = restaurantRepository.findById(reviewRestaurantRegistrationRequestDTO.getId());
        if (restaurantEntityOptional.isPresent()) {
            //Update restaurant review status
            RestaurantEntity restaurantEntityExisting = restaurantEntityOptional.get();
            if (ApprovalStatus.SUBMITTED.value().equals(restaurantEntityExisting.getStatus())) {
                String approvalStatus = reviewRestaurantRegistrationRequestDTO.isApproved() ? ApprovalStatus.APPROVED.value() : ApprovalStatus.REJECTED.value();
                restaurantEntityExisting.updateApprovalStatusAndComment(approvalStatus, reviewRestaurantRegistrationRequestDTO.getApproverComment());
                restaurantRepository.save(restaurantEntityExisting);
            } else {
                log.error("Restaurant with id: {} was already reviewed", reviewRestaurantRegistrationRequestDTO.getId());
                throw new OperationNotAllowedException("Restaurant registration already reviewed");
            }
        } else {
            log.error("Restaurant not found with id : {}", reviewRestaurantRegistrationRequestDTO.getId());
            throw new DataNotFoundException("Restaurant not found");
        }
    }

    public RestaurantDTO handleMenuCreation(CreateMenuDTO createMenuDTO) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(createMenuDTO.getRestaurantId()).orElse(null);
        if (Objects.nonNull(restaurantEntity)) {
            if (ApprovalStatus.APPROVED.value().equals(restaurantEntity.getStatus())) {
                List<MenuItemEntity> menuItems = menuService.prepareMenuItemList(createMenuDTO);
                menuService.saveMenuItems(menuItems);
                RestaurantEntity restaurantEntityCreated = restaurantRepository.findById(createMenuDTO.getRestaurantId()).orElse(null);
                return Objects.nonNull(restaurantEntityCreated) ? restaurantEntityCreated.convertEntityToDTO() : null;
            } else {
                log.error("CreateMenu :: Menu creation request received from an unapproved restaurant with id:{}", createMenuDTO.getRestaurantId());
                throw new OperationNotAllowedException("Menu can't be created for unapproved restaurant");
            }
        } else {
            log.error("CreateMenu :: Restaurant not found in system with id:{}", createMenuDTO.getRestaurantId());
            throw new DataNotFoundException("Restaurant not found in system");
        }

    }
}
