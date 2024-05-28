package com.ordersmicroservice.orders_microservice.services;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.dto.CartProductDto;
import com.ordersmicroservice.orders_microservice.services.impl.CartServiceImpl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartServiceTest {
    private MockWebServer mockWebServer;
    private CartServiceImpl cartServiceImpl;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        RestClient restClient = RestClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        cartServiceImpl = new CartServiceImpl(restClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("When fetching a cart by ID, then the correct cart details are returned")
    void testGetCartById() throws Exception {
        cartServiceImpl.setCartUri("/carts");

        CartDto cartDto = buildCart();

        ObjectMapper objectMapper = new ObjectMapper();
        String cartJson = objectMapper.writeValueAsString(cartDto);

        mockWebServer.enqueue(new MockResponse()
                .setBody(cartJson)
                .addHeader("Content-Type", "application/json"));

        CartDto retrievedCartDto = cartServiceImpl.getCartById(1L).orElseThrow();

        assertThat(retrievedCartDto).isNotNull();
        assertThat(retrievedCartDto.getId()).isEqualTo(1L);
        assertThat(retrievedCartDto.getCartProducts().get(0).getProductName()).isEqualTo("Apple MacBook Pro");
        assertThat(retrievedCartDto.getCartProducts().get(0).getPrice()).isEqualTo(new BigDecimal("2399.99"));
        assertThat(retrievedCartDto.getTotalPrice()).isEqualTo(new BigDecimal("2399.99"));
    }

    @NotNull
    private static CartDto buildCart() {
        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setId(1L);
        cartProductDto.setProductName("Apple MacBook Pro");
        cartProductDto.setProductDescription("Latest model of Apple MacBook Pro 16 inch.");
        cartProductDto.setQuantity(1);
        cartProductDto.setPrice(new BigDecimal("2399.99"));

        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        cartDto.setUserId(1L);
        cartDto.setCartProducts(List.of(cartProductDto));
        cartDto.setTotalPrice(new BigDecimal("2399.99"));
        return cartDto;
    }

    @Test
    @DisplayName("When fetching a non-existent cart by ID, then a 404 error is returned")
    void testGetCartByIdNotFound() {
        cartServiceImpl.setCartUri("/carts");
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("Cart not found")
                .addHeader("Content-Type", "text/plain"));

        assertThatThrownBy(() -> cartServiceImpl.getCartById(1L))
                .isInstanceOf(RestClientResponseException.class)
                .hasMessageContaining("Cart not found")
                .extracting(ex -> ((RestClientResponseException) ex).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    @DisplayName("When fetching a Cart by ID and an internal server error occurs, then a 500 error is returned")
    void testGetCartByIdServerError() {
        cartServiceImpl.setCartUri("/carts");
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
                .addHeader("Content-Type", "text/plain"));

        assertThatThrownBy(() -> cartServiceImpl.getCartById(1L))
                .isInstanceOf(RestClientResponseException.class)
                .hasMessageContaining("Internal Server Error")
                .extracting(ex -> ((RestClientResponseException) ex).getStatusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("When deleting the products in a Cart, the cart must get empty")
    void testEmptyCart() throws InterruptedException {

        cartServiceImpl.setCartUri("/carts");
        Long cartId = 1L;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200));

        cartServiceImpl.emptyCartProductsById(cartId);
        var recordedRequest = mockWebServer.takeRequest();

        assertThat(recordedRequest.getMethod()).isEqualTo("DELETE");
        assertThat(recordedRequest.getPath()).isEqualTo("/carts/" + cartId);
    }
}
