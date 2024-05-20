package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.dto.CartProductDto;
import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.exception.NotFoundException;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.CartService;
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
    private final CartService cartService;




    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService) {

        this.cartService = cartService;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {

        return Optional.of(orderRepository.findAll()).filter(orders -> !orders.isEmpty())
                .orElseThrow(() -> new NotFoundException("No orders were found"));

    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));
    }

    @Override
    public Order addOrder(Long id) {

        CartDto cart = cartService.getCartById(id);
        //List<CartProductDto> cartProducts = cartService.getCartById(id).getCartProducts();
        Order order = new Order();


        order.setCartId(cart.getId());
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderedProducts(cartProducts);
        order.setFromAddress(randomAddress());
        order.setStatus(Status.UNPAID);
        order.setDateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            return orderRepository.save(order);
    }

    private String randomAddress() {
        String[] addresses = {"123 Main St", "456 Elm St", "789 Oak St", "101 Maple Ave", "222 Pine St", "333 Cedar Rd"};
        this.random = new Random();
        return addresses[this.random.nextInt(addresses.length)];
    }

    public Order patchOrder(Long id, @RequestBody Status updatedStatus) {

            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Order not found with ID: " + id));
            existingOrder.setStatus(updatedStatus);
            if (updatedStatus == Status.DELIVERED) {
                existingOrder.setDateDelivered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            return orderRepository.save(existingOrder);
    }


    public void deleteById(Long id) {
        orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found with id: " + id));
        orderRepository.deleteById(id);
    }
}


