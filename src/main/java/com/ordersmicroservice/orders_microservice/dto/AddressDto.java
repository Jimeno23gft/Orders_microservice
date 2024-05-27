package com.ordersmicroservice.orders_microservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {
    private Long id;
    private String cityName;
    private String zipCode;
    private String street;
    private Integer number;
    private String door;
}
