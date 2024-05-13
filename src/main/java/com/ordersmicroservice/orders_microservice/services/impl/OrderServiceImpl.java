package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.Order;
import com.ordersmicroservice.orders_microservice.models.OrderEntity;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public OrderEntity getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    @Override
    public OrderEntity addOrder(OrderEntity order) {
        return orderRepository.save(order);
    }
    public OrderEntity patchOrder(Long id, OrderEntity updatedOrder) {

        OrderEntity existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id)); // Or use a more specific exception

        if (updatedOrder.getStatus() != null) {
            existingOrder.setUser_id(updatedOrder.getUser_id());
            existingOrder.setFrom_address(updatedOrder.getFrom_address());
            existingOrder.setTo_address(updatedOrder.getTo_address());
            existingOrder.setStatus(updatedOrder.getStatus());
            existingOrder.setDate_ordered(updatedOrder.getDate_ordered());
            existingOrder.setDate_delivered(updatedOrder.getDate_delivered());
            return orderRepository.save(existingOrder);
        }
        return null;
    }


    public void deleteById(Long id) {
    orderRepository.deleteById(id);
    }
    }
