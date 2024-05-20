package com.ordersmicroservice.orders_microservice.services.impl;


import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class CartServiceImpl implements CartService {

    private final RestTemplate restTemplate;
    private final String cartServiceUrl;

    public CartServiceImpl(RestTemplate restTemplate, @Value("${cart.service.url}") String cartServiceUrl) {
        this.restTemplate = restTemplate;
        this.cartServiceUrl = cartServiceUrl;
    }

    public CartDto getCartById(Long id) {
        String url = cartServiceUrl + "/carts/" + id;

            return restTemplate.getForObject(url, CartDto.class);
        }
}


