package com.ordersmicroservice.orders_microservice.dto;

import lombok.Data;

@Data
public class CountryDto {
    private Long id;
    private String name;
    private Float tax;
    private String prefix;
    private String timeZone;
}
