package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    @Override
    public Order addOrder(Long id) {

        Order order = new Order();
        order.setUser_id(id);
        order.setFrom_address(RandomAndress());
        order.setStatus(Status.UNPAID);
        order.setDate_ordered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return orderRepository.save(order);
    }

    private String RandomAndress() {
        String[] adresses = {"123 Main St","456 Elm St","789 Oak St","101 Maple Ave","222 Pine St","333 Cedar Rd"};
        Random random = new Random();
        return adresses[random.nextInt(adresses.length)];
    }

    public Order patchOrder(Long id, Status updatedStatus) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));

            existingOrder.setStatus(updatedStatus);
            if(updatedStatus == Status.DELIVERED){
                existingOrder.setDate_delivered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }

            return orderRepository.save(existingOrder);
    }


    public void deleteById(Long id) {
    orderRepository.deleteById(id);
    }
    }


