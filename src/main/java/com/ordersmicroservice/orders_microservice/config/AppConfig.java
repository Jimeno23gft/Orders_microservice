package com.ordersmicroservice.orders_microservice.config;

import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestClient;

@Configuration
@Generated
@EnableRetry
public class AppConfig {
    @Value("${cart.url}")
    private String urlCart;
    @Bean
    public RestClient.Builder restClientBuilder(){
        return RestClient.builder().baseUrl(urlCart);
    }
    @Bean
    public RestClient restClient(){
        return RestClient.create();
    }

}