package com.ordersmicroservice.orders_microservice.services.impl;


import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.services.CartService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class CartServiceImpl implements CartService {

    private final RestClient restClient;

    public CartServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    public CartDto getCartById(Long id){

        return restClient.get()
                .uri("/carts/" + id)
                .retrieve()
                .body(CartDto.class);
    }

}
