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
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class CountryServiceTest {

    private MockWebServer mockWebServer;
    private CountryServiceImpl countryServiceImpl;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        RestClient restClient = RestClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        countryServiceImpl = new CountryServiceImpl(restClient);
    }

    @Test
    @DisplayName("Testing method retrieves country with given id")
    void testGetCountryById() {

        countryServiceImpl.cartUri = "/country";

        String countryJson = """
                {
                        "id": 1,
                        "name": "España",
                        "tax": 21,
                        "prefix": "+34",
                        "timeZone": "Europe/Madrid"
                }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(countryJson)
                .addHeader("Content-Type", "application/json"));

        CountryDto countryDto = countryServiceImpl.getCountryById(1L);

        assertNotNull(countryDto);
        assertEquals(1L, countryDto.getId());
        assertEquals("España", countryDto.getName());
    }

    @Test
    @DisplayName("When fetching a non-existent country by ID, then a 404 error is returned")
    void testGetUserByIdNotFound() {

        countryServiceImpl.cartUri = "/country";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("User not found")
                .addHeader("Content-Type", "text/plain"));
        RestClientResponseException exception = assertThrows(RestClientResponseException.class, () -> countryServiceImpl.getCountryById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("When fetching a Country by ID and an internal server error occurs, then a 500 error is returned")
    void testGetCountryByIdServerError() {

        countryServiceImpl.cartUri = "/country";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
                .addHeader("Content-Type", "text/plain"));
        RestClientResponseException exception = assertThrows(RestClientResponseException.class, () -> countryServiceImpl.getCountryById(1L));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());

    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

}
