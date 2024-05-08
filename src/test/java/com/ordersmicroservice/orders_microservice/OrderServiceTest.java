package com.ordersmicroservice.orders_microservice;

import com.ordersmicroservice.orders_microservice.api.models.Order;
import com.ordersmicroservice.orders_microservice.api.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.api.services.OrderService;
import com.ordersmicroservice.orders_microservice.api.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    public void testGetAllUsers(){
        when(orderRepository.findAll()).thenReturn(List.of(new Order()));
        List<Order> orders = orderService.getAllOrders();

        assertEquals(1, orders.size());
    }
}
