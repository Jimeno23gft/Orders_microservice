package com.ordersmicroservice.orders_microservice.services.impl;


import com.ordersmicroservice.orders_microservice.dto.UserDto;
import com.ordersmicroservice.orders_microservice.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
    private final WebClient webClient;

    public UserServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<UserDto> getUserById(Long userId) {
        return webClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserDto.class);
    }
}