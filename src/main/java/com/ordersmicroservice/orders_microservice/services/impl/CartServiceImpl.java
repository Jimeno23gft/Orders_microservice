package com.ordersmicroservice.orders_microservice.services.impl;


import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.services.CartService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;


@Service
public class CartServiceImpl implements CartService {

    @Getter
    @Setter
    private String cartUri = "http://localhost:8081/carts/";

    private final RestClient restClient;

    public CartServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    public Optional<CartDto> getCartById(Long id){

        return Optional.ofNullable(restClient.get()
                .uri(cartUri + id)
                .retrieve()
                .body(CartDto.class));
    }

    public void emptyCartProductsById(Long id){

        restClient.delete()
                .uri(cartUri + "/{id}", id)
                .retrieve()
                .body(Void.class);
    }
}


