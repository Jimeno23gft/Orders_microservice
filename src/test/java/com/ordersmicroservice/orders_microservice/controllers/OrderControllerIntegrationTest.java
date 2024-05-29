package com.ordersmicroservice.orders_microservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.dto.CartProductDto;
import com.ordersmicroservice.orders_microservice.dto.CreditCardDto;
import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.models.Order;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OrderControllerIntegrationTest {


    @Autowired
    private WebTestClient webTestClient;

    private static MockWebServer mockWebServer;

    private Order expectedOrder;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8081);
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();

    }

    @BeforeEach
    void setUp() throws IOException {


        expectedOrder = Order.builder()
                .id(7L)
                .userId(101L)
                .cartId(1L)
                .fromAddress("222 Pine St")
                .status(Status.UNPAID)
                .dateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .dateDelivered(null) // Since dateDelivered is null initially
                .totalPrice(new BigDecimal("323.3"))
                .orderedProducts(new ArrayList<>()).build();
    }



    @Test
    void addOrderIntegrationTest() throws JsonProcessingException {


        List<CartProductDto> cartProductDtoList = new ArrayList<>();
        CartProductDto cartProductDto1 = CartProductDto.builder()
                .id(1L)
                .productName("Apple MacBook Pro")
                .productDescription("Latest model of Apple MacBook Pro 16 inch.")
                .quantity(1)
                .price(new BigDecimal("2399.99"))
                .build();
        CartProductDto cartProductDto2 = CartProductDto.builder()
                .id(2L)
                .productName("Logitech Mouse")
                .productDescription("Wireless Logitech Mouse M235")
                .price(new BigDecimal("29.99"))
                .quantity(2)
                .build();

        cartProductDtoList.add(cartProductDto1);
        cartProductDtoList.add(cartProductDto2);

        CartDto cartDto = CartDto.builder()
                .id(1L)
                .userId(101L)
                //.cartId(1L)
                .totalPrice(new BigDecimal("323.3"))
                .cartProducts(cartProductDtoList)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String cartJson = objectMapper.writeValueAsString(cartDto);

        mockWebServer.enqueue(new MockResponse()
                .setBody(cartJson)
                .addHeader("Content-Type", "application/json"));
        mockWebServer.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 204 No Content")
                .addHeader("Content-Type", "application/json"));


        Long cartId = 1L;
        CreditCardDto creditCardDto = new CreditCardDto(new BigInteger("1111111111"),"09/25", 222);



        webTestClient.post().uri("/orders/{id}", cartId )
                .bodyValue(creditCardDto)
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
    }
}
