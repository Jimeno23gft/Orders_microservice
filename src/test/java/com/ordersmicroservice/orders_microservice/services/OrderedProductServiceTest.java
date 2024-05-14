package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.repositories.OrderedProductRepository;
import com.ordersmicroservice.orders_microservice.services.impl.OrderedProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderedProductServiceTest {
    @Mock
    OrderedProductRepository orderedProductRepository;
    @InjectMocks
    OrderedProductServiceImpl orderedProductsService;
    Long orderId;
    private OrderedProduct orderedProduct1;
    private OrderedProduct orderedProduct2;
    private List<OrderedProduct> orderedProducts;

    @BeforeEach
    void setup(){
        orderId = 1L;
        orderedProduct1 = OrderedProduct.builder()
                .orderId(orderId)
                .productId(1L)
                .quantity(3)
                .build();
        orderedProduct2 = OrderedProduct.builder()
                .orderId(orderId)
                .productId(2L)
                .quantity(5)
                .build();
        orderedProducts = List.of(orderedProduct1, orderedProduct2);
    }

    @Test
    void testGetAllProductsFromOrder(){
        when(orderedProductRepository.findByOrderId(orderId)).thenReturn(orderedProducts);

        List<OrderedProduct> savedProducts = orderedProductsService.getAllProductsFromOrder(orderId);
        assertThat(savedProducts)
                .isNotNull()
                .isNotEqualTo(Collections.emptyList())
                .isEqualTo(orderedProducts);
    }
}
