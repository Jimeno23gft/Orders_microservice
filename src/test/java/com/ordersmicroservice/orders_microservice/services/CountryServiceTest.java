package com.ordersmicroservice.orders_microservice.services;


import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void testGetCountryById() throws Exception {

        countryServiceImpl.countrytUri = "/country";

        CountryDto countryDto = new CountryDto();
        countryDto.setId(1L);
        countryDto.setName("España");
        countryDto.setTax(21F);
        countryDto.setPrefix("+34");
        countryDto.setTimeZone("Europe/Madrid");

        ObjectMapper objectMapper = new ObjectMapper();
        String countryJson = objectMapper.writeValueAsString(countryDto);

        mockWebServer.enqueue(new MockResponse()
                .setBody(countryJson)
                .addHeader("Content-Type", "application/json"));

        CountryDto retrievedCountryDto = countryServiceImpl.getCountryById(1L).orElseThrow();

        assertThat(retrievedCountryDto).isNotNull();
        assertThat(retrievedCountryDto.getId()).isEqualTo(1L);
        assertThat(retrievedCountryDto.getName()).isEqualTo("España");
        assertThat(retrievedCountryDto.getTax()).isEqualTo(21);
        assertThat(retrievedCountryDto.getPrefix()).isEqualTo("+34");
        assertThat(retrievedCountryDto.getTimeZone()).isEqualTo("Europe/Madrid");
    }

    @Test
    @DisplayName("When fetching a non-existent country by ID, then a 404 error is returned")
    void testGetUserByIdNotFound() {

        countryServiceImpl.countrytUri = "/country";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("User not found")
                .addHeader("Content-Type", "text/plain"));

        assertThatThrownBy(() -> countryServiceImpl.getCountryById(1L))
                .isInstanceOf(RestClientResponseException.class)
                .hasMessageContaining("User not found")
                .extracting(ex -> ((RestClientResponseException) ex).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("When fetching a Country by ID and an internal server error occurs, then a 500 error is returned")
    void testGetCountryByIdServerError() {

        countryServiceImpl.countrytUri = "/country";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
                .addHeader("Content-Type", "text/plain"));

        assertThatThrownBy(() -> countryServiceImpl.getCountryById(1L))
                .isInstanceOf(RestClientResponseException.class)
                .hasMessageContaining("Internal Server Error")
                .extracting(ex -> ((RestClientResponseException) ex).getStatusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}
