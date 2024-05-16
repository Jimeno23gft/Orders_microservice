package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.UserDto;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDto> getUserById(Long userId);
}
