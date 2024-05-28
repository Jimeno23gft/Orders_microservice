package com.ordersmicroservice.orders_microservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ordersmicroservice.orders_microservice.models.Address;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String phone;

    public static UserResponseDto fromUserDto(UserDto user) {
        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        return userResponse;
    }
}

