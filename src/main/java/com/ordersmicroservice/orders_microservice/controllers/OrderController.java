package com.ordersmicroservice.orders_microservice.controllers;

import com.ordersmicroservice.orders_microservice.dto.Order;
import com.ordersmicroservice.orders_microservice.models.OrderEntity;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id){return orderService.getOrderById(id);}

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        orderService.deleteById(id);
    }


}
