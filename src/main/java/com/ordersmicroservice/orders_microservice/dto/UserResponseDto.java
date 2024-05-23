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

    public static UserResponseDto fromUserDto(UserDto userDto) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(userDto.getId());
        userResponseDto.setName(userDto.getName());
        userResponseDto.setEmail(userDto.getEmail());
        return userResponseDto;
    }
}

