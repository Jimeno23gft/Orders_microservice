package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.CountryDto;
import reactor.core.publisher.Mono;

public interface CountryService {

    CountryDto getCountryById(Long countryId);

}
