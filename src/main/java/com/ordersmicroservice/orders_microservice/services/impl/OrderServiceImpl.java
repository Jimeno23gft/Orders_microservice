package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.exception.GlobalExceptionHandler;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    Random random;

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
        order.setCartId(id);
        order.setFromAddress(randomAddress());
        order.setStatus(Status.UNPAID);
        order.setDateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            return orderRepository.save(order);
        } catch (GlobalExceptionHandler.BadRequest ex) {
            throw new GlobalExceptionHandler.BadRequest("");
        }
    }

    private String randomAddress() {
<<<<<<< HEAD
<<<<<<< Updated upstream
        String[] adresses = {"123 Main St","456 Elm St","789 Oak St","101 Maple Ave","222 Pine St","333 Cedar Rd"};

        return adresses[this.random.nextInt(adresses.length)];
=======
        String[] addresses = {"123 Main St", "456 Elm St", "789 Oak St", "101 Maple Ave", "222 Pine St", "333 Cedar Rd"};
        this.random = new Random();
        return addresses[random.nextInt(addresses.length)];
>>>>>>> Stashed changes
=======
        String[] addresses = {"123 Main St", "456 Elm St", "789 Oak St", "101 Maple Ave", "222 Pine St", "333 Cedar Rd"};
        Random random = new Random();
        return addresses[random.nextInt(addresses.length)];
>>>>>>> 279cc33f62c1a3d44ed6f806ea5440c66aa7fbcd
    }

    public Order patchOrder(Long id, @RequestBody Status updatedStatus) {
        try {

<<<<<<< HEAD
<<<<<<< Updated upstream
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
=======
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("Order not found with id " + id));
>>>>>>> Stashed changes
=======
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("Order not found with id " + id));
            Status previousStatus = existingOrder.getStatus();
>>>>>>> 279cc33f62c1a3d44ed6f806ea5440c66aa7fbcd

            existingOrder.setStatus(updatedStatus);
            if (updatedStatus == Status.DELIVERED) {
                existingOrder.setDateDelivered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
<<<<<<< Updated upstream

<<<<<<< HEAD
=======
>>>>>>> Stashed changes
=======

>>>>>>> 279cc33f62c1a3d44ed6f806ea5440c66aa7fbcd
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


