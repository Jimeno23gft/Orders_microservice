package com.ordersmicroservice.orders_microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.dto.StatusUpdateDto;
import com.ordersmicroservice.orders_microservice.exception.NotFoundException;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    MockMvc mockMvc;
    @MockBean
    OrderService orderService;
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        OrderController orderController = new OrderController(orderService);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    @DisplayName("Testing to get mock orders")
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
    @DisplayName("Testing get a mock order by ID")
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
    @DisplayName("Testing posting a order")
    void testPostNewOrder() throws Exception {

        Long cart_id = 1L;

        when(orderService.addOrder(cart_id)).thenAnswer(invocation -> {
            Order order = new Order();
            order.setCartId(1L);
            order.setFromAddress("Madrid");
            order.setStatus(Status.DELIVERED);
            order.setDateOrdered("2001-01-21");
            order.setDateDelivered("2002-01-21");
            return order;
        });

        mockMvc.perform(post("/orders/{id}", cart_id))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cartId", is(cart_id.intValue())))
                .andExpect(jsonPath("$.fromAddress", is("Madrid")))
                .andExpect(jsonPath("$.dateOrdered", is("2001-01-21")));

        verify(orderService).addOrder(cart_id);
    }

    @Test
    @DisplayName("Testing deleting a order by ID")
    void testDeleteById() throws Exception {

        Long id = 3L;

        mockMvc.perform(delete("/orders/{id}", id))
                .andExpect(status().isOk());

        verify(orderService).deleteById(id);

    }

    @Test
    @DisplayName("Testing deleting a order that doesn't exists")
    void testDeleteByIdShouldFailWhenIdNotFound() throws Exception {
        Long id = 33L;
        doThrow(new NotFoundException("Order not found")).when(orderService).deleteById(id);

        assertThrows(NotFoundException.class, () -> orderService.deleteById(id));
        verify(orderService).deleteById(id);
    }
    @Test
    @DisplayName("Testing patch an order to status PAID")
    void testPatchOrder () throws Exception {

        Long id = 1L;
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto();
        statusUpdateDto.setStatus(Status.PAID);

        Order mockOrder = new Order();
        mockOrder.setId(id);
        mockOrder.setStatus(Status.PAID);

        when(orderService.patchOrder(eq(id), any(Status.class))).thenReturn(mockOrder);

        mockMvc.perform(patch("/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService).patchOrder(id, statusUpdateDto.getStatus());

    }


}
