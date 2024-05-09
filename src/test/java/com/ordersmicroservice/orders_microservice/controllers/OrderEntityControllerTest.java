package com.ordersmicroservice.orders_microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordersmicroservice.orders_microservice.dto.Order;
import com.ordersmicroservice.orders_microservice.models.OrderEntity;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderEntityControllerTest {
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
        OrderController orderController = new OrderController(orderService);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
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
        Order orderToPost = new Order(null, 1L, "Madrid", "Zaragoza", DELIVERED, "2001-21-21", "2002-21-21");

        when(orderService.addOrder(any())).then(invocationOnMock -> {
            Order order = invocationOnMock.getArgument(0);
            order.setId(7L);
            return order;
        });

        mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderToPost)))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.from_address", is("Madrid")))
                .andExpect(jsonPath("$.date_ordered", is("2001-21-21")));

        verify(orderService).addOrder(any());

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
