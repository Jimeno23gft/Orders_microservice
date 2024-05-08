package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.models.OrderEntity;

import java.util.List;

public interface OrderService {
    List<OrderEntity> getAllOrders();
    OrderEntity getOrderById(Long orderId);
}
