package com.ordersmicroservice.orders_microservice.models;

import jakarta.persistence.*;
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
@IdClass(OrderedProductEntityId.class)
public class OrderedProductEntity {

    @Id
    @Column(name = "order_id")
    Long order_id;

    @Id
    @Column(name = "product_id")
    Long product_id;

    @Column(name = "quantity")
    Integer quantity;
}
