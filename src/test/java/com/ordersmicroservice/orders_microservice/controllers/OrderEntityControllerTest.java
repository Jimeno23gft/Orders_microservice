package com.ordersmicroservice.orders_microservice.controllers;

import com.ordersmicroservice.orders_microservice.models.OrderEntity;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
public class OrderEntityControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    OrderService orderService;
    @MockBean
    OrderRepository orderRepository;
    List<OrderEntity> orderEntities;

    @BeforeEach
    void setup() {
        //create order
        //orders = List.of(new Order(1234, 9876, "", "", "", "", "", ""))
    }
/*
    @Test
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk());
        when(orderRepository.findAll()).thenReturn(orderEntities);
    }

    @Test
    void testGetOrderById() throws Exception {
        long orderId = 1;
        OrderEntity expectedOrderEntity = new OrderEntity();

        MvcResult result = mockMvc.perform(get("/api/orders/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedOrderEntity.toString()))
                .andReturn();
    }
*/
    @Test
    void probarGetAll() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        when(orderRepository.findAll()).thenReturn(orderEntities);
    }
}
