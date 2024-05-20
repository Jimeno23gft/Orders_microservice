package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.CartDto;
import reactor.core.publisher.Mono;

public interface CartService {

    CartDto getCartById(Long id);
}
