package com.ordersmicroservice.orders_microservice.services;


import com.ordersmicroservice.orders_microservice.dto.CountryDto;
import com.ordersmicroservice.orders_microservice.services.impl.CountryServiceImpl;
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

public class CountryServiceTest {

    private MockWebServer mockWebServer;
    private CountryServiceImpl countryServiceImpl;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        countryServiceImpl = new CountryServiceImpl(webClient);
    }

    @Test
    void testGetCountryById() {
        String countryJson = """
                {
                        "id": 1,
                        "name": "Espanya",
                        "tax": 21,
                        "prefix": "+34",
                        "timeZone": "Europe/Madrid"
                }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(countryJson)
                .addHeader("Content-Type", "application/json"));

        Mono<CountryDto> countryMono = countryServiceImpl.getCountryById(1L);


        StepVerifier.create(countryMono)
                .expectNextMatches(country ->
                                country.getName().equals("Espanya"))
                .verifyComplete();

    }

    @Test
    @DisplayName("When fetching a non-existent country by ID, then a 404 error is returned")
    void testGetUserByIdNotFound() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("User not found")
                .addHeader("Content-Type", "text/plain"));
        Mono<CountryDto> countryMono = countryServiceImpl.getCountryById(717L);
        StepVerifier.create(countryMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.NOT_FOUND)
                .verify();
    }

    @Test
    @DisplayName("When fetching a Country by ID and an internal server error occurs, then a 500 error is returned")
    void testGetCountryByIdServerError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
                .addHeader("Content-Type", "text/plain"));
        Mono<CountryDto> countryMono = countryServiceImpl.getCountryById(1L);

        StepVerifier.create(countryMono)
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
