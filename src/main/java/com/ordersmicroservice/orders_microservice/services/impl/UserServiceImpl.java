package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.UserDto;
import com.ordersmicroservice.orders_microservice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final RestClient restClient;

    public String userUri = "http://localhost:8082/users/";

    public UserServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }
    public Optional<UserDto> getUserById(Long userId) {
        log.info("getUserById( userId:  {} )",userId);
        return Optional.ofNullable(restClient.get()
                .uri(userUri + "/{id}", userId)
                .retrieve()
                .body(UserDto.class));
    }

    public void patchFidelityPoints(Long userId, int points){
        String url = "http://localhost:8082/fidelitypoints/";

        restClient.patch()
                .uri(url + "{id}", userId)
                .body(points)
                .retrieve();
    }
}