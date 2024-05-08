package com.ordersmicroservice.orders_microservice.repositories;

import com.ordersmicroservice.orders_microservice.models.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
}
