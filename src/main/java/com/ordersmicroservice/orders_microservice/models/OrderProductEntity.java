package com.ordersmicroservice.orders_microservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orderedProducts")
public class OrderProductEntity {

    @Id
    @Column(name = "order_id")
    Long order_id;

    @Id
    @Column(name = "product_id")
    Long product_id;

    @Column(name = "quantity")
    Long quantity;
}
