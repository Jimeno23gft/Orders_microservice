package com.ordersmicroservice.orders_microservice.services.impl;


import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.services.CartService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class CartServiceImpl implements CartService {

    public String cartUri = "http://localhost:8081/carts/";

    private final RestClient restClient;

    public CartServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    public CartDto getCartById(Long id){

        return restClient.get()
                .uri(cartUri + id)
                .retrieve()
                .body(CartDto.class);
    }

    public void emptyCartProductsById(Long id){

        restClient.delete()
                .uri(cartUri + "/{id}", id)
                .retrieve()
                .body(Void.class);
    }
}


