package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.UserDto;
import com.ordersmicroservice.orders_microservice.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UserServiceImpl implements UserService {
    private final RestClient restClient;

    public String userUri = "http://localhost:8082/users/";

    public UserServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }
    public UserDto getUserById(Long userId) {
        return restClient.get()
                .uri(userUri + "/{id}", userId)
                .retrieve()
                .body(UserDto.class);
    }
}