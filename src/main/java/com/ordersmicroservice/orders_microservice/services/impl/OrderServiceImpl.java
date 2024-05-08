package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.models.OrderEntity;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Override
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public OrderEntity getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }


}
