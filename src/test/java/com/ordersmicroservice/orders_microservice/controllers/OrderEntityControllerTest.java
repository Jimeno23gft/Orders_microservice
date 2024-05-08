package com.ordersmicroservice.orders_microservice.controllers;

<<<<<<< HEAD:src/test/java/com/ordersmicroservice/orders_microservice/controllers/OrderEntityControllerTest.java
import com.ordersmicroservice.orders_microservice.models.OrderEntity;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.OrderService;
=======
import com.ordersmicroservice.orders_microservice.api.controllers.OrderController;
import com.ordersmicroservice.orders_microservice.api.models.Order;
import com.ordersmicroservice.orders_microservice.api.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.api.services.OrderService;
>>>>>>> bdc50305c21ccf85b95837e9dfc3ca981933dfa9:src/test/java/com/ordersmicroservice/orders_microservice/OrderControllerTest.java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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
<<<<<<< HEAD:src/test/java/com/ordersmicroservice/orders_microservice/controllers/OrderEntityControllerTest.java
    List<OrderEntity> orderEntities;

    @BeforeEach
    void setup() {
=======
    List<Order> orders;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
>>>>>>> bdc50305c21ccf85b95837e9dfc3ca981933dfa9:src/test/java/com/ordersmicroservice/orders_microservice/OrderControllerTest.java
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
<<<<<<< HEAD:src/test/java/com/ordersmicroservice/orders_microservice/controllers/OrderEntityControllerTest.java
        OrderEntity expectedOrderEntity = new OrderEntity();
=======
        String expectedResponseBody = "{\"order_id\":1,\"user_id\":1,\"product_id\":1,\"from_address_id\":1,\"to_address_id\":1,\"status\":\"UNPAID\",\"date_ordered\":\"08-05-2024\",\"date_delivered\":\"08-05-2024\"}";
>>>>>>> bdc50305c21ccf85b95837e9dfc3ca981933dfa9:src/test/java/com/ordersmicroservice/orders_microservice/OrderControllerTest.java

        mockMvc.perform(get("/api/orders/{id}", orderId))
                .andExpect(status().isOk())
<<<<<<< HEAD:src/test/java/com/ordersmicroservice/orders_microservice/controllers/OrderEntityControllerTest.java
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
=======
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
>>>>>>> bdc50305c21ccf85b95837e9dfc3ca981933dfa9:src/test/java/com/ordersmicroservice/orders_microservice/OrderControllerTest.java
    }
}
