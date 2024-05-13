package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.Order;
import com.ordersmicroservice.orders_microservice.models.OrderEntity;

import java.util.List;

public interface OrderService {
    List<OrderEntity> getAllOrders();
    OrderEntity getOrderById(Long orderId);
    OrderEntity addOrder(OrderEntity order);
    void deleteById(Long id);
    OrderEntity patchOrder(Long id, OrderEntity mockOrder);

}
