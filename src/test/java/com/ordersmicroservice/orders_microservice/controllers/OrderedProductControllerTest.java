package com.ordersmicroservice.orders_microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.services.OrderedProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderedProductController.class)
public class OrderedProductControllerTest {
    MockMvc mockMvc;
    @MockBean
    OrderedProductService orderedProductService;

    ObjectMapper objectMapper;

    @BeforeEach
    void test(){
        objectMapper = new ObjectMapper();
        OrderedProductController orderedProductController = new OrderedProductController(orderedProductService);
        mockMvc = MockMvcBuilders.standaloneSetup(orderedProductController).build();
    }

    @Test
    void getAllProductsFromOrderTest() throws Exception {
        OrderedProduct orderedProduct1 = OrderedProduct
                .builder()
                .order_id(1L)
                .product_id(1L)
                .quantity(3).build();
        OrderedProduct orderedProduct2 = OrderedProduct
                .builder()
                .order_id(1L)
                .product_id(2L)
                .quantity(5).build();
        OrderedProduct orderedProduct3 = OrderedProduct
                .builder()
                .order_id(2L)
                .product_id(1L)
                .quantity(2).build();
        List<OrderedProduct> orderedProducts = Arrays.asList(orderedProduct1, orderedProduct2, orderedProduct3);
        when(orderedProductService.getAllProductsFromOrder()).thenReturn(orderedProducts);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/products").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].order_id").value(1L))
                .andExpect(jsonPath("$[1].order_id").value(1L))
                .andExpect(jsonPath("$[2].order_id").value(2L))
                .andExpect(jsonPath("$[0].product_id").value(1L))
                .andExpect(jsonPath("$[1].product_id").value(2L))
                .andExpect(jsonPath("$[2].product_id").value(1L))
                .andExpect(jsonPath("$[0].quantity").value(3))
                .andExpect(jsonPath("$[1].quantity").value(5))
                .andExpect(jsonPath("$[2].quantity").value(2))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(content().json(objectMapper.writeValueAsString(orderedProducts)));
        verify(orderedProductService, times(1)).getAllProductsFromOrder();
    }
}
