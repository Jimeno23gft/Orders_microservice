package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.UserDto;
import com.ordersmicroservice.orders_microservice.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import reactor.test.StepVerifier;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest {

    private MockWebServer mockWebServer;
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        RestClient restClient = RestClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        userServiceImpl = new UserServiceImpl(restClient);
    }

    @Test
    @DisplayName("When fetching a user by ID, " +
            "then the correct user details are returned")
    void testGetUserById() {
        String userJson = """
                {
                    "id": 100,
                    "name": "John",
                    "lastName": "Doe",
                    "email": "john.doe@example.com",
                    "password": "password123",
                    "fidelityPoints": 1000,
                    "birthDate": "1990/01/01",
                    "phone": "1234567890",
                    "address": {\s
                        "id": 1,\s
                        "cityName": "Madrid",\s
                        "zipCode": "47562",\s
                        "street": "C/ La Coma",
                        "number": 32,
                        "door": "1A"
                    },\s
                    "country": {
                        "id": 1,\s
                        "name": "España",\s
                        "tax": 21,\s
                        "prefix": "+34",\s
                        "timeZone": "Europe/Madrid"\s
                    }
                }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(userJson)
                .addHeader("Content-Type", "application/json"));

        UserDto userDto = userServiceImpl.getUserById(100L);

        assertEquals(100L,(long) userDto.getId());
        assertEquals("1234567890",userDto.getPhone());
        assertEquals(21,userDto.getCountry().getTax());
        assertEquals("Madrid",userDto.getAddress().getCityName());
    }

    @Test
    @DisplayName("When fetching a non-existent user by ID, then a 404 error is returned")
    void testGetUserByIdNotFound() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("User not found")
                .addHeader("Content-Type", "text/plain"));
        Exception exception = assertThrows(RestClientResponseException.class, () -> {
            userServiceImpl.getUserById(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, ((RestClientResponseException) exception).getStatusCode());
    }

    @Test
    @DisplayName("When fetching a User by ID and an internal server error occurs, then a 500 error is returned")
    void testGetProductByIdServerError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
                .addHeader("Content-Type", "text/plain"));
        Exception exception = assertThrows(RestClientResponseException.class, () -> {
            userServiceImpl.getUserById(1L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((RestClientResponseException) exception).getStatusCode());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

}
