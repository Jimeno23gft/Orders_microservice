package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.UserDto;

public interface UserService {
    UserDto getUserById(Long userId);
}
