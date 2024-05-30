package com.ordersmicroservice.orders_microservice.services.impl;
import com.ordersmicroservice.orders_microservice.dto.CountryDto;
import com.ordersmicroservice.orders_microservice.services.CountryService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    private final RestClient restClient;
    public String countrytUri = "https://user-microservice-ey3npq3qvq-uc.a.run.app/country/";

    public CountryServiceImpl( RestClient restClient) {
        this.restClient = restClient;
    }
    public Optional<CountryDto> getCountryById(Long countryId) {
        return Optional.ofNullable(restClient.get()
                .uri(countrytUri + "/{id}", countryId)
                .retrieve()
                .body(CountryDto.class));
    }
}
