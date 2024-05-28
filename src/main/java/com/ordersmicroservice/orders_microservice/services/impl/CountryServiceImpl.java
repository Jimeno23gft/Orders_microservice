package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.CountryDto;
import com.ordersmicroservice.orders_microservice.services.CountryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CountryServiceImpl implements CountryService {
    private final RestClient restClient;

    public CountryServiceImpl( RestClient restClient) {
        this.restClient = restClient;
    }
    public CountryDto getCountryById(Long countryId) {
        return restClient.get()
                .uri("/country/{id}", countryId)
                .retrieve()
                .body(CountryDto.class);
    }
}
