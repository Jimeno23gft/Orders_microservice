package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.models.OrderedProductEntity;
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
    private OrderedProductEntity orderedProduct1;
    private OrderedProductEntity orderedProduct2;
    private List<OrderedProductEntity> orderedProducts;

    @BeforeEach
    void setup(){
        orderedProduct1 = OrderedProductEntity.builder()
                .order_id(1L)
                .product_id(1L)
                .quantity(3)
                .build();
        orderedProduct2 = OrderedProductEntity.builder()
                .order_id(1L)
                .product_id(2L)
                .quantity(5)
                .build();
        orderedProducts = List.of(orderedProduct1, orderedProduct2);
    }

    @Test
    void testGetAllProductsFromOrder(){
        when(orderedProductRepository.findAll()).thenReturn(orderedProducts);

        List<OrderedProductEntity> savedProducts = orderedProductsService.getAllProductsFromOrder();
        assertThat(savedProducts)
                .isNotNull()
                .isNotEqualTo(Collections.emptyList())
                .isEqualTo(orderedProducts);
    }
}
