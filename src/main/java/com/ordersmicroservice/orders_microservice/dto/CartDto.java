package com.ordersmicroservice.orders_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {


        private Long id;

        @JsonProperty("user_id")
        private Long userId;
        private List<CartProductDto> cartProducts = new ArrayList<>();;
        private BigDecimal totalPrice;

}
