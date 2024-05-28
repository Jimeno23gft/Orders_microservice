package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.CountryDto;

public interface CountryService {
    CountryDto getCountryById(Long countryId);
}
