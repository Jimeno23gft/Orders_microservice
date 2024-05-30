package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.UserDto;
import com.ordersmicroservice.orders_microservice.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final RestClient restClient;

    public String userUri = "https://user-microservice-ey3npq3qvq-uc.a.run.app/users/";

    public UserServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }
    public Optional<UserDto> getUserById(Long userId) {
        return Optional.ofNullable(restClient.get()
                .uri(userUri + "/{id}", userId)
                .retrieve()
                .body(UserDto.class));
    }

    public void patchFidelityPoints(Long userId, int points){
        String url = "https://user-microservice-ey3npq3qvq-uc.a.run.app/fidelitypoints/";

        restClient.patch()
                .uri(url + "{id}", userId)
                .body(points)
                .retrieve();
    }
}