package com.ordersmicroservice.orders_microservice.dto;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    @Id
    private Long id;
    private String productName;
    private String productDescription;
    private Integer quantity;
    private BigDecimal price;

}
