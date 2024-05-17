package com.ordersmicroservice.orders_microservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@IdClass(OrderedProductId.class)
public class OrderedProduct {

    @Id
    @Column(name = "order_id")
    Long orderId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @Id
    @Column(name = "product_id")
    Long productId;

    @Column(name = "name")
    String name;

    @Column(name = "category")
    String category;

    @Column(name = "description")
    String description;

    @Column(name = "price")
    Long price;

    @Column(name = "quantity")
    Integer quantity;
}

