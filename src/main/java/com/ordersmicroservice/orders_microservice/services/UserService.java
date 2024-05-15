package com.ordersmicroservice.orders_microservice.services;
import com.ordersmicroservice.orders_microservice.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserDto> getUserById(Long userId);
}
