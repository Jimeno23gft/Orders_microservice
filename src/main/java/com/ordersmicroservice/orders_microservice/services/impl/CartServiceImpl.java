package com.ordersmicroservice.orders_microservice.services.impl;


import com.ordersmicroservice.orders_microservice.dto.CartDto;

import lombok.extern.slf4j.Slf4j;

import com.ordersmicroservice.orders_microservice.services.CartService;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    private final WebClient webClient;

    public CartServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<CartDto> getCartById(Long id){

        return webClient.get()
                .uri("/carts/" + id)
                .retrieve()
                .bodyToMono(CartDto.class)
                .doOnNext(cart -> log.info("Recuperando carrito con Id: {} del usuario con Id: {}", cart.getId(), cart.getUserId()));
    }

}
