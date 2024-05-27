package com.ordersmicroservice.orders_microservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private Integer fidelityPoints;
    private String birthDate;
    private String phone;
    private AddressDto address;
    private CountryDto country;
}
