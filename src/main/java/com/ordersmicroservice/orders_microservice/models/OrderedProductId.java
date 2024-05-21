package com.ordersmicroservice.orders_microservice.models;

import lombok.Generated;

import java.io.Serializable;

@Generated
public class OrderedProductId implements Serializable {
    private Long orderId; //NOSONAR Necessary for SQL
    private Long productId; //NOSONAR Necessary for SQL
}