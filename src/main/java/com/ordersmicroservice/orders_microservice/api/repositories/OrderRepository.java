package com.ordersmicroservice.orders_microservice.api.repositories;

import com.ordersmicroservice.orders_microservice.api.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
