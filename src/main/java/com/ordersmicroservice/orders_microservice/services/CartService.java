package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.CartDto;

public interface CartService {

    CartDto getCartById(Long id);
    void emptyCartProductsById(Long id);
}
