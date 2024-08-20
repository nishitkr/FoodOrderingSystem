package com.nishit.FoodOrdering.service;

import com.nishit.FoodOrdering.exception.ServiceException;
import com.nishit.FoodOrdering.dto.UserDTO;
import com.nishit.FoodOrdering.constants.ErrorCode;
import com.nishit.FoodOrdering.dto.request.RegisterUserDTO;
import com.nishit.FoodOrdering.entity.UserEntity;
import com.nishit.FoodOrdering.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO handleUserRegistration(RegisterUserDTO registerUserDTO) {
        UserEntity registeredUserEntity;
        try  {
            registeredUserEntity = userRepository.save(UserEntity.builder()
                            .id(UUID.randomUUID().toString())
                            .name(registerUserDTO.getName())
                            .email(registerUserDTO.getEmail())
                            .phone(registerUserDTO.getPhone())
                            .address(registerUserDTO.getAddress())
                    .build());
            return registeredUserEntity.convertEntityToDTO();
        } catch(Exception ex) {
            log.error("Exception occurred while registering the user : " + registerUserDTO.getName(), ex);
            throw new ServiceException("Exception occurred while registering the user", ex);
        }
    }
}
