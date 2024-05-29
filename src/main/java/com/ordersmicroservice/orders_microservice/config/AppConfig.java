package com.ordersmicroservice.orders_microservice.config;

import lombok.Generated;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestClient;

@Configuration
@Generated
@EnableRetry
public class AppConfig {
    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

}