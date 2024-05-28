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
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
    void testGetCartById() {

        cartServiceImpl.setCartUri("/carts");
        String cartJson = """
                {
                    "id": 1,
                    "user_id":1,
                    "cart_id": 101,
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
                    ],
                    "totalPrice": 2399.99
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(cartJson)
                .addHeader("Content-Type", "application/json"));

        CartDto cartDto = cartServiceImpl.getCartById(1L).orElseThrow();

        assertEquals(1L, (long) cartDto.getId());
        assertEquals("Apple MacBook Pro", cartDto.getCartProducts().get(0).getProductName());
        assertEquals(new BigDecimal("2399.99"), cartDto.getCartProducts().get(0).getPrice());
        assertEquals(new BigDecimal("2399.99"), cartDto.getTotalPrice());
    }

    @Test
    @DisplayName("When fetching a non-existent cart by ID, then a 404 error is returned")
    void testGetCartByIdNotFound() {
        cartServiceImpl.setCartUri("/carts");
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("Cart not found")
                .addHeader("Content-Type", "text/plain"));

        RestClientResponseException exception = assertThrows(RestClientResponseException.class, () -> cartServiceImpl.getCartById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }


    @Test
    @DisplayName("When fetching a Cart by ID and an internal server error occurs, then a 500 error is returned")
    void testGetCartByIdServerError() {
        cartServiceImpl.setCartUri("/carts");
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
                .addHeader("Content-Type", "text/plain"));


        RestClientResponseException exception = assertThrows(RestClientResponseException.class, () -> cartServiceImpl.getCartById(1L));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
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
        assertEquals("DELETE", recordedRequest.getMethod());
        assertEquals("/carts/" + cartId, recordedRequest.getPath());

    }
}
