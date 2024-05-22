package com.ordersmicroservice.orders_microservice.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CreditCardDto {
    private BigInteger cardNumber;
    private String expirationDate;
    private int CVCCode;
}
