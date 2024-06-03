package com.ordersmicroservice.orders_microservice.services.impl;
import com.ordersmicroservice.orders_microservice.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ProductServiceImpl {

    private final RestClient restClient;

    public String catalogUri = "http://localhost:8083/catalog/products/";

    public ProductServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

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
}
