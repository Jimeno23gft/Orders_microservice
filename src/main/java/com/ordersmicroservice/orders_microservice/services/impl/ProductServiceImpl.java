package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.ProductDto;
import com.ordersmicroservice.orders_microservice.exception.RetryFailedConnection;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ProductServiceImpl {

    private final RestClient restClient;

    public String catalogUri = "http://localhost:8083/catalog/products/";

    public ProductServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Retryable(retryFor = RetryFailedConnection.class, maxAttempts = 2, backoff = @Backoff(delay = 2000))
    public ProductDto patchProductStock(Long productId, int quantity) {
        return restClient.patch()
                .uri(catalogUri+"/{id}/{quantity}", productId, quantity)
                .retrieve()
                .body(ProductDto.class);
    }

    public ProductDto getProductById(Long productId) {
        return restClient.get()
                .uri(catalogUri+"/{id}", productId)
                .retrieve()
                .body(ProductDto.class);
    }
    @Recover
    public String recover(RetryFailedConnection exception){
        return "Server down try later";
    }
}
