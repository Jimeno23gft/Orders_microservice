package com.ordersmicroservice.orders_microservice.repositories;

import com.ordersmicroservice.orders_microservice.models.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProductRepository extends JpaRepository<OrderProductEntity, Long> {
}
