package com.ordersmicroservice.orders_microservice.services.impl;


import com.ordersmicroservice.orders_microservice.dto.CartDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class CartServiceImpl {



    private final WebClient webClient;

    public CartServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<CartDto> getCartById(Long id){

        return webClient.get()
                .uri("/carts/" + id)
                .retrieve()
                .bodyToMono(CartDto.class);
    }

}
