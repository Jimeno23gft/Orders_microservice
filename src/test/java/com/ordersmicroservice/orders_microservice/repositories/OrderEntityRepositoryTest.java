package com.ordersmicroservice.orders_microservice.repositories;

import com.ordersmicroservice.orders_microservice.models.OrderEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class OrderEntityRepositoryTest {
    @Autowired
    public OrderRepository orderRepository;
/*
    @Test
    public void testFindAll(){
        List<OrderEntity> orderEntities = new ArrayList<>();
        orderRepository.saveAll(orderEntities);
        assertNotNull(orderEntities);
    }
    @Test
    public void testFindById(){

    }

 */
}
