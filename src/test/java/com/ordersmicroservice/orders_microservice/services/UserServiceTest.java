package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.UserDto;
import com.ordersmicroservice.orders_microservice.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;

import java.io.IOException;

class UserServiceTest {
    private MockWebServer mockWebServer;
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        userServiceImpl = new UserServiceImpl(webClient);
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

        Mono<UserDto> userMono = userServiceImpl.getUserById(100L);

        StepVerifier.create(userMono)
                .expectNextMatches(user ->
                        user.getId().equals(100L) &&
                                user.getPhone().equals("1234567890")
                                && user.getName().equals("John"))
                .verifyComplete();

    }

    @Test
    @DisplayName("When fetching a non-existent user by ID, then a 404 error is returned")
    void testGetUserByIdNotFound() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("User not found")
                .addHeader("Content-Type", "text/plain"));
        Mono<UserDto> userMono = userServiceImpl.getUserById(999L);
        StepVerifier.create(userMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.NOT_FOUND)
                .verify();
    }

    @Test
    @DisplayName("When fetching a User by ID and an internal server error occurs, then a 500 error is returned")
    void testGetProductByIdServerError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
                .addHeader("Content-Type", "text/plain"));
        Mono<UserDto> userMono = userServiceImpl.getUserById(1L);

        StepVerifier.create(userMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verify();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

}
