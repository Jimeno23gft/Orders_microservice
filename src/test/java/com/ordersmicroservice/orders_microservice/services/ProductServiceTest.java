package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.ProductDto;
import com.ordersmicroservice.orders_microservice.services.impl.ProductServiceImpl;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import okhttp3.mockwebserver.MockResponse;

import java.io.IOException;

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
    @DisplayName("Testing method updates the stock of a given product")
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
    @DisplayName("Testing method fails to find the order with id given to be updated")
    void testPatchIdNotFound() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
                .setBody("Not found")
                .addHeader("Content-Type", "application/json"));

        RestClientResponseException exception = assertThrows(RestClientResponseException.class, () -> productService.patchProductStock(1L, -5));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Testing method gives an Internal Server Error")
    void testPatchServerError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody("Internal Server Error")
                .addHeader("Content-Type", "application/json"));

        RestClientResponseException exception = assertThrows(RestClientResponseException.class, () -> productService.patchProductStock(1L, -5));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    @DisplayName("Testing method retrieves the product with given id")
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
        assertEquals(15, productDto.getCurrentStock());
    }

    @Test
    @DisplayName("Testing method fails to find the product with id given to be retrieved")
    void testGetProductByIdNotFound() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
                .setBody("Not found")
                .addHeader("Content-Type", "application/json"));

        RestClientResponseException exception = assertThrows(RestClientResponseException.class, () -> productService.getProductById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Testing method gives an error whenever product with id given is called for")
    void testGetProductByIdError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody("Internal Server Error")
                .addHeader("Content-Type", "application/json"));

        RestClientResponseException exception = assertThrows(RestClientResponseException.class, () -> productService.getProductById(1L));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

}
