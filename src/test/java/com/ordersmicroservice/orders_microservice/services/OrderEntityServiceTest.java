package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.models.OrderEntity;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderEntityServiceTest {
    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderServiceImpl orderService;
/*
    @Test
    public void testGetAllUsers(){
        when(orderRepository.findAll()).thenReturn(List.of(new OrderEntity()));
        List<OrderEntity> orderEntities = orderService.getAllOrders();

        assertEquals(1, orderEntities.size());
    }

 */
}
