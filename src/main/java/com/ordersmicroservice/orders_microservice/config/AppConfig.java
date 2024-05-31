package com.ordersmicroservice.orders_microservice.config;

import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.client.RestClient;

@Configuration
@Generated
public class AppConfig {
    @Value("${cart.url}")
    private String URL_CART;
    @Bean
    public RestClient.Builder restClientBuilder(){
        return RestClient.builder().baseUrl(URL_CART);
    }
    @Bean
    public RestClient restClient(){
        return RestClient.create();
    }

}