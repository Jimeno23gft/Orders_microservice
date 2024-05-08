package com.ordersmicroservice.orders_microservice;

import com.ordersmicroservice.orders_microservice.controllers.OrderController;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    OrderService orderService;
    @MockBean
    OrderRepository orderRepository;
    List<Order> orders;
    @BeforeEach
    void setup(){
        //create order
        //orders = List.of(new Order(1234, 9876, "", "", "", "", "", ""))
    }

    @Test
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk());
        when(orderRepository.findAll()).thenReturn(orders);
    }

    @Test
    void testGetOrderById() throws Exception {
        long orderId = 1;
        Order expectedOrder = new Order();

        MvcResult result = mockMvc.perform(get("/api/orders/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedOrder.toString()))
                .andReturn();
}
