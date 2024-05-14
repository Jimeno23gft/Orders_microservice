package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.services.impl.CartServiceImpl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;

public class CartServiceTest {


        private MockWebServer mockWebServer;
        private CartServiceImpl cartService;

        @BeforeEach
        void setUp() throws IOException {

            mockWebServer = new MockWebServer();
            mockWebServer.start();

            WebClient webClient = WebClient.builder()
                    .baseUrl(mockWebServer.url("/").toString())
                    .build();

            cartService = new CartServiceImpl(webClient);
        }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("When fetching a cart by ID, then the correct cart details are returned")
    void testGetCartById() {

        String cartJson = """
            {
                "id": 1,
                "user_id": 101,
                "updated_at": "2024-05-01T10:00:00.000+00:00",
                "cartProducts": [
                    {
                        "id": 1,
                        "productName": "Apple MacBook Pro",
                        "productCategory": "Electronics",
                        "productDescription": "Latest model of Apple MacBook Pro 16 inch.",
                        "quantity": 1,
                        "price": 2399.99
                    }
                ]
            }
            """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(cartJson)
                .addHeader("Content-Type", "application/json"));

        Mono<CartDto> cartMono = cartService.getCartById(1L);

        StepVerifier.create(cartMono)
                .expectNextMatches(cartDto ->
                        cartDto.getId().equals(1L) &&
                                cartDto.getUserId().equals(101L) &&
                                cartDto.getCartProducts().size() == 1 &&
                                cartDto.getCartProducts().get(0).getProductName().equals("Apple MacBook Pro") &&
                                cartDto.getCartProducts().get(0).getQuantity().equals(1) &&
                                cartDto.getCartProducts().get(0).getPrice().equals(2399.99)
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("When fetching a non-existent cart by ID, then a 404 error is returned")
    void testGetCartByIdNotFound() {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("Cart not found")
                .addHeader("Content-Type", "text/plain"));


        Mono<CartDto> cartMono = cartService.getCartById(999L);

        StepVerifier.create(cartMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.NOT_FOUND)
                .verify();
    }


    @Test
    @DisplayName("When fetching a Cart by ID and an internal server error occurs, then a 500 error is returned")
    void testGetCartByIdServerError() {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
                .addHeader("Content-Type", "text/plain"));


        Mono<CartDto> cartMono = cartService.getCartById(1L);

        StepVerifier.create(cartMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verify();
    }


















}
