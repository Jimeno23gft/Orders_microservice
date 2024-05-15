package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.CartDto;
import reactor.core.publisher.Mono;

public interface CartService {

    Mono<CartDto> getCartById(Long id);
}