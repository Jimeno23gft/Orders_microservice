package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.ProductDto;
import com.ordersmicroservice.orders_microservice.services.impl.ProductServiceImpl;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private MockWebServer mockWebServer;
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        RestClient restClient = RestClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        productService = new ProductServiceImpl(restClient);
    }

    @Test
    void testPatchProductStock() {
        String productJson = """
        {
            "id": 1,
            "name": "Ball",
            "description": "Red Ball",
            "price": 15,
            "category_Id": 0,
            "weight": 0,
            "current_stock": 15,
            "min_stock": 0
        }
        """;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody(productJson)
                .addHeader("Content-Type", "application/json"));

        ProductDto productDto = productService.patchProductStock(1L, -5);

        assertNotNull(productDto);
        assertEquals(1L, productDto.getId());
        assertEquals("Ball", productDto.getName());
        assertEquals(15, productDto.getCurrentStock());
    }

    @Test
    void testPatchIdNotFound() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
                .setBody("Not found")
                .addHeader("Content-Type", "application/json"));

        Exception exception = assertThrows(RestClientResponseException.class, () -> {
            productService.patchProductStock(1L, -5);
        });

        assertEquals(HttpStatus.NOT_FOUND, ((RestClientResponseException) exception).getStatusCode());
    }

    @Test
    void testPatchServerError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody("Internal Server Error")
                .addHeader("Content-Type", "application/json"));

        Exception exception = assertThrows(RestClientResponseException.class, () -> {
            productService.patchProductStock(1L, -5);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((RestClientResponseException) exception).getStatusCode());
    }

    @Test
    void testGetProductById() {
        String productJson = """
        {
            "id": 1,
            "name": "Ball",
            "description": "Red Ball",
            "price": 15,
            "category_Id": 0,
            "weight": 0,
            "current_stock": 15,
            "min_stock": 0
        }
        """;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody(productJson)
                .addHeader("Content-Type", "application/json"));

        ProductDto productDto = productService.getProductById(1L);

        assertNotNull(productDto);
        assertEquals(1L, productDto.getId());
        assertEquals("Ball", productDto.getName());
        assertEquals(15, productDto.getCurrentStock());;
    }

    @Test
    void testGetProductByIdNotFound() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
                .setBody("Not found")
                .addHeader("Content-Type", "application/json"));

        Exception exception = assertThrows(RestClientResponseException.class, () -> {
            productService.getProductById(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, ((RestClientResponseException) exception).getStatusCode());
    }

    @Test
    void testGetProductByIdError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody("Internal Server Error")
                .addHeader("Content-Type", "application/json"));

        Exception exception = assertThrows(RestClientResponseException.class, () -> {
            productService.getProductById(1L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((RestClientResponseException) exception).getStatusCode());
    }

}
