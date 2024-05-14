package com.ordersmicroservice.orders_microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static com.ordersmicroservice.orders_microservice.Datos.crearOrder001;
import static com.ordersmicroservice.orders_microservice.Datos.crearOrder002;
import static com.ordersmicroservice.orders_microservice.dto.Status.DELIVERED;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    MockMvc mockMvc;
    @MockBean
    OrderService orderService;
    @MockBean
    OrderRepository orderRepository;
    List<Order> orderEntities;
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        OrderController orderController = new OrderController(orderService);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

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
        mockMvc.perform(get("/orders/{id}", id).contentType(MediaType.APPLICATION_JSON))

                //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PAID"));

        verify(orderService).getOrderById(1L);
    }

    @Test
    void testPostNewOrder() throws Exception {

        Long user_id = 1L;

        when(orderService.addOrder(user_id)).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            Order order = new Order();
            order.setUser_id(1L);
            order.setFrom_address("Madrid");
            order.setTo_address("Zaragoza");
            order.setStatus(Status.DELIVERED);
            order.setDate_ordered("2001-01-21");
            order.setDate_delivered("2002-01-21");
            return order;
        });

        mockMvc.perform(post("/orders/{id}", user_id))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id", is(user_id.intValue())))
                .andExpect(jsonPath("$.from_address", is("Madrid")))
                .andExpect(jsonPath("$.date_ordered", is("2001-01-21")));

        verify(orderService).addOrder(user_id);

    }

    @Test
    void testDeleteById() throws Exception {

        Long id = 3L;

        mockMvc.perform(delete("/orders/{id}", id))
                .andExpect(status().isNoContent());


        verify(orderService).deleteById(id);

    }

    @Test
    void testDeleteByIdShouldFailWhenIdNotFound() throws Exception {
        Long id = 3L;
        doThrow(new EntityNotFoundException("Order not found")).when(orderService).deleteById(id);

        mockMvc.perform(delete("/orders/{id}", id))
                .andExpect(status().isNotFound());

        verify(orderService).deleteById(id);
    }

    @Test
    void testPatchOrder () throws Exception {
        Long id = 1L;

        Order mockOrder = crearOrder001().orElseThrow();

        when(orderService.patchOrder(id, mockOrder)).thenReturn(mockOrder);

        mockMvc.perform(patch("/orders/{id}", id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mockOrder)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService).patchOrder(id, mockOrder);

    }
}
