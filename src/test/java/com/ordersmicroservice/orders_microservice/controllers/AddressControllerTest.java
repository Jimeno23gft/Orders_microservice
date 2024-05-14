package com.ordersmicroservice.orders_microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordersmicroservice.orders_microservice.models.Address;
import com.ordersmicroservice.orders_microservice.repositories.AddressRepository;
import com.ordersmicroservice.orders_microservice.services.AddressService;
import com.ordersmicroservice.orders_microservice.services.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
public class AddressControllerTest {
    MockMvc mockMvc;
    @MockBean
    AddressServiceImpl addressService;
    Address address;
    ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
        AddressController addressController = new AddressController(addressService);
        mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();
    }

    @Test
    void testGetToAddressFromOrder() throws Exception {
        Address mockAddress = Address.builder()
                .addressId(1L)
                .orderId(1L)
                .street("a")
                .number(1)
                .door("1A")
                .cityName("A")
                .zipCode("11111").build();
        when(addressService.getToAddressFromOrder(1L)).thenReturn(mockAddress);

        mockMvc.perform(get("/orders/address/" + 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.addressId").value(1L))
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.street").value("a"))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.door").value("1A"))
                .andExpect(jsonPath("$.cityName").value("A"))
                .andExpect(jsonPath("$.zipCode").value("11111"))
                .andExpect(content().json(objectMapper.writeValueAsString(mockAddress)));

        verify(addressService).getToAddressFromOrder(mockAddress.getOrderId());
    }
}
