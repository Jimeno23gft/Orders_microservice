package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.CountryDto;
import com.ordersmicroservice.orders_microservice.dto.UserDto;
import com.ordersmicroservice.orders_microservice.exception.RetryFailedConnection;
import com.ordersmicroservice.orders_microservice.services.CountryService;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    private final RestClient restClient;
    public String countrytUri = "http://localhost:8082/country/";

    public CountryServiceImpl( RestClient restClient) {
        this.restClient = restClient;
    }
    @Retryable(retryFor = RetryFailedConnection.class, maxAttempts = 2, backoff = @Backoff(delay = 2000))
    public Optional<CountryDto> getCountryById(Long countryId) {
        return Optional.ofNullable(restClient.get()
                .uri(countrytUri + "/{id}", countryId)
                .retrieve()
                .body(CountryDto.class));
    }
    @Recover
    public Optional<CountryDto> recover(RetryFailedConnection exception, Long userId){
        System.out.println("Recovery method executed for userId: " + userId);
        return Optional.empty();
    }
}
