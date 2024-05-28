package com.ordersmicroservice.orders_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditCardDto {
    private BigInteger cardNumber;
    private String expirationDate;
    private int CVCCode;
}
