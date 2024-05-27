package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.CountryDto;
import com.ordersmicroservice.orders_microservice.services.CountryService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CountryServiceImpl implements CountryService {
    private final RestClient restClient;
    public String cartUri = "http://localhost:8082/country";

    public CountryServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    public CountryDto getCountryById(Long countryId) {
        return restClient.get()
                .uri(cartUri + "/{id}", countryId)
                .retrieve()
                .body(CountryDto.class);
    }
}
