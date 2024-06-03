package com.ordersmicroservice.orders_microservice.services.impl;
import com.ordersmicroservice.orders_microservice.dto.CountryDto;
import com.ordersmicroservice.orders_microservice.services.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Service
public class CountryServiceImpl implements CountryService {
    private final RestClient restClient;
    public String countrytUri = "http://localhost:8082/country/";

    public CountryServiceImpl( RestClient restClient) {
        this.restClient = restClient;
    }
    public Optional<CountryDto> getCountryById(Long countryId) {
        log.info("CountryServiceImpl: getCountryById ( countryId = " + countryId + " )");
        return Optional.ofNullable(restClient.get()
                .uri(countrytUri + "/{id}", countryId)
                .retrieve()
                .body(CountryDto.class));
    }
}
