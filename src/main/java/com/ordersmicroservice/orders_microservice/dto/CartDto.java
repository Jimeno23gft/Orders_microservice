package com.ordersmicroservice.orders_microservice.dto;

import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import lombok.*;
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
        @JsonProperty("cart_id")
        private Long cartId;
        private List<CartProductDto> cartProducts = new ArrayList<>();
        private BigDecimal totalPrice;


}
