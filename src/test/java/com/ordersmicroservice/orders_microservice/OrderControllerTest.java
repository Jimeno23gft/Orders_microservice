package com.ordersmicroservice.orders_microservice;

import com.ordersmicroservice.orders_microservice.api.controllers.OrderController;
import com.ordersmicroservice.orders_microservice.api.models.Order;
import com.ordersmicroservice.orders_microservice.api.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.api.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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
    void setup() {
        MockitoAnnotations.openMocks(this);
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
        String expectedResponseBody = "{\"order_id\":1,\"user_id\":1,\"product_id\":1,\"from_address_id\":1,\"to_address_id\":1,\"status\":\"UNPAID\",\"date_ordered\":\"08-05-2024\",\"date_delivered\":\"08-05-2024\"}";

        mockMvc.perform(get("/api/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void testGetOrderByIdWithNonexistentId() throws Exception {
        long nonExistentId = -1;
        mockMvc.perform(get("/api/orders/{id}", nonExistentId)).andExpect(status().isNotFound());
    }

    @Test
    void testGetOrderByIdWithInvalidId() throws Exception {
        String invalidId = "abc";

        mockMvc.perform(get("/api/orders/{id}", invalidId))
                .andExpect(status().isBadRequest());
    }
}
