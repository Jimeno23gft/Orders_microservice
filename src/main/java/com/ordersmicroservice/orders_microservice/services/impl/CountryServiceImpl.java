package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.CountryDto;


import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CountryServiceImpl {
    private final WebClient webClient;

    public CountryServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<CountryDto> getCountryById(Long countryId) {
        return webClient.get()
                .uri("/country/{id}", countryId)
                .retrieve()
                .bodyToMono(CountryDto.class)
                .doOnNext(country -> log.info("Recuperado pais con ID: {}, Nombre: {}",
                country.getId(), country.getName()));
    }
}
