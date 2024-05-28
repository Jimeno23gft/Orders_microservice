package com.ordersmicroservice.orders_microservice.services.impl;


import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.services.CartService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class CartServiceImpl implements CartService {

    public String cartUri = "/carts/";

    private final RestClient restClient;

    public CartServiceImpl(RestClient.Builder restClient) {
        this.restClient = restClient.build();
    }

    public static CartDto getCartById(Long id){

        return restClient.get()
                .uri(cartUri + id)
                .retrieve()
                .body(CartDto.class);
    }

    public static void emptyCartProductsById(Long id){

        restClient.delete()
                .uri(cartUri + "/{id}", id)
                .retrieve();
    }
}


