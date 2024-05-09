package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.Order;

import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ordersmicroservice.orders_microservice.dto.Status.DELIVERED;
import static com.ordersmicroservice.orders_microservice.dto.Status.IN_DELIVERY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderEntityServiceTest {
    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderServiceImpl orderService;
    private Order order1;
    private Order order2;
    private List<Order> orders;

    @BeforeEach
    public void setup() {
        order1 = Order.builder()
                .id(1L)
                .user_id(1L)
                .from_address("a")
                .to_address("b")
                .status(DELIVERED)
                .date_ordered("2024-5-9")
                .date_delivered("2024-5-10").build();
        order2 = Order.builder()
                .id(2L)
                .user_id(2L)
                .from_address("c")
                .to_address("d")
                .status(IN_DELIVERY)
                .date_ordered("2024-5-11")
                .date_delivered("2024-5-12").build();
        orders = List.of(order1, order2);
    }

    @Test
    public void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> savedOrders = orderService.getAllOrders();
        assertNotNull(savedOrders);
        assertNotEquals(savedOrders, Collections.emptyList());
        assertEquals(orders, savedOrders);
    }

    @Test
    public void testGetOrderById() {
        when(orderRepository.findById(order1.getId())).thenReturn(Optional.of(order1));

        Order savedOrder = orderService.getOrderById(order1.getId());
        assertNotNull(savedOrder);
        assertEquals(order1, savedOrder);
    }
}
