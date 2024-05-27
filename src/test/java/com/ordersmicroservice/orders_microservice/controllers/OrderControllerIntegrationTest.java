package com.ordersmicroservice.orders_microservice.controllers;

import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
 class OrderControllerIntegrationTest {


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    private Order expectedOrder;

    @BeforeEach
    void setUp(){

        expectedOrder = Order.builder()
                .id(7L)
                .userId(101L)
                .cartId(1L)
                .fromAddress("222 Pine St")
                .status(Status.UNPAID)
                .dateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .dateDelivered(null) // Since dateDelivered is null initially
                .totalPrice(new BigDecimal("323.3"))
                .orderedProducts(Arrays.asList(
                        OrderedProduct.builder()
                                .productId(1L)
                                .name("Apple MacBook Pro")
                                .category("Electronics")
                                .description("Latest model of Apple MacBook Pro 16 inch.")
                                .price(new BigDecimal("2399.99"))
                                .quantity(1)
                                .build(),
                        OrderedProduct.builder()
                                .productId(2L)
                                .name("Logitech Mouse")
                                .category("Electronics")
                                .description("Wireless Logitech Mouse M235")
                                .price(new BigDecimal("29.99"))
                                .quantity(2)
                                .build()
                ))
                .build();
    }

    @Disabled("Test requires cart microservice to be executed")
    @DisplayName("Integration test that proves the correct creation of an order with cartId an the Products from cart_microservice")
    @Test
    void addOrderIntegrationTest() {

        Long cartId = 1L;

        webTestClient.post().uri("http://localhost:8080/orders/{id}", cartId )
                .bodyValue(expectedOrder)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Order.class)
                .value(responseOrder -> {
                    assertThat(responseOrder.getUserId()).isEqualTo(expectedOrder.getUserId());
                    assertThat(responseOrder.getTotalPrice()).isEqualTo(expectedOrder.getTotalPrice());
                    assertThat(responseOrder.getStatus()).isEqualTo(Status.PAID);
                    assertThat(responseOrder.getOrderedProducts()).hasSize(2);
                    assertThat(responseOrder.getOrderedProducts().get(0).getName()).isEqualTo("Apple MacBook Pro");
                    assertThat(responseOrder.getOrderedProducts().get(1).getName()).isEqualTo("Logitech Mouse");
                });

        webTestClient.get().uri("http://localhost:8083/catalog/{id}", cartId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.cartProducts").isEmpty();

    }
}
