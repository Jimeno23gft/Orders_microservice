package com.ordersmicroservice.orders_microservice;

import com.ordersmicroservice.orders_microservice.api.models.Order;
import com.ordersmicroservice.orders_microservice.api.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    public OrderRepository orderRepository;

    @Test
    public void testFindAll(){
        List<Order> orders = new ArrayList<>();
        orderRepository.saveAll(orders);
        assertNotNull(orders);
    }
    @Test
    public void testFindById(){

    }
}
