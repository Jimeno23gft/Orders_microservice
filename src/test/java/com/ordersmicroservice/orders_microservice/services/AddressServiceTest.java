package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.models.Address;
import com.ordersmicroservice.orders_microservice.repositories.AddressRepository;
import com.ordersmicroservice.orders_microservice.services.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    AddressRepository addressRepository;
    @InjectMocks
    AddressServiceImpl addressService;
    private Address address;
    private Long orderId;

    @BeforeEach
    void setup(){
        address = Address.builder()
                .addressId(1L)
                .orderId(1L)
                .street("a")
                .number(1)
                .door("1A")
                .cityName("a")
                .zipCode("123")
                .build();
    }

    @Test
    void testGetToAddressFromOrder(){
        when(addressRepository.findByOrderId(orderId)).thenReturn(address);

        Address savedAddress = addressService.getToAddressFromOrder(orderId);
        assertThat(savedAddress)
                .isNotNull()
                .isEqualTo(address);
    }
}
