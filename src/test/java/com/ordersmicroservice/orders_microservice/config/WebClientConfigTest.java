package com.ordersmicroservice.orders_microservice.config;

import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WebClientConfigTest {

    @Test
    public void testWebClient() {

        WebClient.Builder builder = Mockito.mock(WebClient.Builder.class);
        Mockito.when(builder.baseUrl(Mockito.anyString())).thenReturn(builder);
        Mockito.when(builder.build()).thenReturn(Mockito.mock(WebClient.class));

        WebClientConfig config = new WebClientConfig();
        WebClient client = config.webClient(builder);

        assertNotNull(client);
        Mockito.verify(builder).baseUrl("http://localhost:8080");
        Mockito.verify(builder).build();
    }
}
