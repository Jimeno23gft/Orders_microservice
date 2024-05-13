package com.ordersmicroservice.orders_microservice.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

public class OrderProductEntityId implements Serializable {
    private Long order_id;
    private Long product_id;
}