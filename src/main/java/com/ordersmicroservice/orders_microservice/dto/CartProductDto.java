package com.ordersmicroservice.orders_microservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProductDto {

    private Long id;
    private String productName;
    private Integer quantity;
    private BigDecimal price;


}
