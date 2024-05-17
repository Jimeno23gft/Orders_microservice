package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    @Override
    public Order addOrder(Long id) {

        Order order = new Order();
        order.setCartId(id);
        order.setFromAddress(randomAddress());
        order.setStatus(Status.UNPAID);
        order.setDateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return orderRepository.save(order);
    }

    private String randomAddress() {
<<<<<<< Updated upstream
        String[] adresses = {"123 Main St","456 Elm St","789 Oak St","101 Maple Ave","222 Pine St","333 Cedar Rd"};

        return adresses[this.random.nextInt(adresses.length)];
=======
        String[] addresses = {"123 Main St", "456 Elm St", "789 Oak St", "101 Maple Ave", "222 Pine St", "333 Cedar Rd"};
        this.random = new Random();
        return addresses[random.nextInt(addresses.length)];
>>>>>>> Stashed changes
    }

    public Order patchOrder(Long id, @RequestBody Status updatedStatus) {

<<<<<<< Updated upstream
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
=======
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("Order not found with id " + id));
>>>>>>> Stashed changes

            existingOrder.setStatus(updatedStatus);
            if(updatedStatus == Status.DELIVERED){
                existingOrder.setDateDelivered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
            return orderRepository.save(existingOrder);
    }


    public void deleteById(Long id) {
    orderRepository.deleteById(id);
    }
    }


