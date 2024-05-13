package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(Long orderId);
    Order addOrder(Order order);
    void deleteById(Long id);
    Order patchOrder(Long id, Order mockOrder);

}
