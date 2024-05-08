package com.ordersmicroservice.orders_microservice.repositories;

import com.ordersmicroservice.orders_microservice.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
