package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.UserDto;
import com.ordersmicroservice.orders_microservice.exception.RetryFailedConnection;
import com.ordersmicroservice.orders_microservice.services.UserService;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final RestClient restClient;

    public String userUri = "http://localhost:8082/users/";

    public UserServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }
    @Retryable(retryFor = RetryFailedConnection.class, maxAttempts = 2, backoff = @Backoff(delay = 2000), exclude = HttpClientErrorException.class)
    public Optional<UserDto> getUserById(Long userId) {
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

    @Recover
    public String recover(RetryFailedConnection exception){
        throw new RetryFailedConnection("Connection Failed");
    }
}