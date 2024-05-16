package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.exception.GlobalExceptionHandler;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {

        return Optional.of(orderRepository.findAll()).filter(orders -> !orders.isEmpty())
                .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("No orders were found"));

    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("Order not found with ID: " + orderId));
    }

    @Override
    public Order addOrder(Long id) {

        try {
            Order order = new Order();
            order.setUser_id(id);
            order.setFrom_address(RandomAddress());
            order.setStatus(Status.UNPAID);
            order.setDate_ordered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            return orderRepository.save(order);
        } catch (GlobalExceptionHandler.BadRequest ex) {
            throw new GlobalExceptionHandler.BadRequest("");
        }
    }

    private String RandomAddress() {
        String[] addresses = {"123 Main St", "456 Elm St", "789 Oak St", "101 Maple Ave", "222 Pine St", "333 Cedar Rd"};
        Random random = new Random();
        return addresses[random.nextInt(addresses.length)];
    }

    public Order patchOrder(Long id, Order updatedOrder) {
        try {
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("Order not found with id: " + id));

            existingOrder.setStatus(updatedOrder.getStatus());
            if (updatedOrder.getStatus() == Status.DELIVERED) {
                existingOrder.setDate_delivered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            return orderRepository.save(existingOrder);
        } catch (GlobalExceptionHandler.BadRequest ex) {
            throw new GlobalExceptionHandler.BadRequest("");
        }
    }


    public void deleteById(Long id) {
        orderRepository.findById(id).orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("Order not found with id: " + id));
        orderRepository.deleteById(id);
    }
}


