package com.ordersmicroservice.orders_microservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ordersmicroservice.orders_microservice.models.Address;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    @JsonIgnore
    private Integer fidelityPoints;
    private String phone;
    private Address address;
    private CountryDto country;
}
