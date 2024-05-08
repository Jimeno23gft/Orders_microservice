package com.ordersmicroservice.orders_microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordersmicroservice.orders_microservice.dto.Order;
import com.ordersmicroservice.orders_microservice.models.OrderEntity;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.ordersmicroservice.orders_microservice.Datos.crearOrder001;
import static com.ordersmicroservice.orders_microservice.Datos.crearOrder002;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderEntityControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    OrderService orderService;
    @MockBean
    OrderRepository orderRepository;
    List<OrderEntity> orderEntities;

    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        //create order
        //orders = List.of(new Order(1234, 9876, "", "", "", "", "", ""))
        objectMapper = new ObjectMapper();
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
    void testGetAllOrders() throws Exception {
        List<Order> mockOrders = Arrays.asList(crearOrder001().orElseThrow(),
                                                     crearOrder002().orElseThrow());
        when(orderService.getAllOrders()).thenReturn(mockOrders);

        mockMvc.perform(get("/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[0].status").value("PAID"))
                .andExpect(jsonPath("$[1].status").value("UNPAID"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(mockOrders)));

        verify(orderService).getAllOrders();
    }

    @Test
    void testGetOrderById() throws Exception {
        Long id = 1L;
        when(orderService.getOrderById(1L)).thenReturn(crearOrder001().orElseThrow());

        //When
        mockMvc.perform(get("/orders/{id}",id).contentType(MediaType.APPLICATION_JSON))

                //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PAID"));

        verify(orderService).getOrderById(1L);
    }
}
