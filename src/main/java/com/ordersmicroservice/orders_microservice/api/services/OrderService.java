package com.ordersmicroservice.orders_microservice.api.services;

import com.ordersmicroservice.orders_microservice.api.models.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(Long orderId);
}
