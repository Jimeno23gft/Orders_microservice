package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.models.OrderedProductEntity;

import java.util.List;

public interface OrderedProductService {
    List<OrderedProductEntity> getAllProductsFromOrder();
}
