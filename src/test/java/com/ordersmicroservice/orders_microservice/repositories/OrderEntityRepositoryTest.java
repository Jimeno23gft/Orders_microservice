package com.ordersmicroservice.orders_microservice.repositories;

import com.ordersmicroservice.orders_microservice.dto.Order;
import com.ordersmicroservice.orders_microservice.models.OrderEntity;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ordersmicroservice.orders_microservice.dto.Order.Status.PAID;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class OrderEntityRepositoryTest {
    @Autowired
    public OrderRepository orderRepository;

    @Test
    public void testFindAll() {
        List<Order> orderEntities = orderRepository.findAll();
        assertNotNull(orderEntities);
        assertFalse(orderEntities.isEmpty());
        assertEquals(6, orderEntities.size());
    }

    @Test
    void testFindById() {
        Optional<Order> order = orderRepository.findById(1L);
        assertTrue(order.isPresent());
        assertEquals("PAID", order.orElseThrow().getStatus().toString());
    }
/*
    @Test
    void testFindByFromAdress() {
        Optional<Order> order = orderRepository.findByStatus("UNKNOWN");
        assertTrue(order.isPresent());
        assertEquals(5, order.orElseThrow().getId());
        assertEquals("2024-05-11", order.orElseThrow().getDate_ordered());
    }

 */
}
