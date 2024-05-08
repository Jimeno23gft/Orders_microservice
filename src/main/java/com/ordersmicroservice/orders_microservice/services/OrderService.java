package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.models.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(Long orderId);
}
