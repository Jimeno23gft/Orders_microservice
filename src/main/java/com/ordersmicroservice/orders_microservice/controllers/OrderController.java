package com.ordersmicroservice.orders_microservice.controllers;

import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("")
    public List<Order> getAllOrders(){
        return null;
    }
}
