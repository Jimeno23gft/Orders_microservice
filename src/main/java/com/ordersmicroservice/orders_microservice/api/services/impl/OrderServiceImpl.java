package com.ordersmicroservice.orders_microservice.api.services.impl;

import com.ordersmicroservice.orders_microservice.api.models.Order;
import com.ordersmicroservice.orders_microservice.api.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }


}
