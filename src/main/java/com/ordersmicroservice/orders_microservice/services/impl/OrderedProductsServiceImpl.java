package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.models.OrderedProductEntity;
import com.ordersmicroservice.orders_microservice.repositories.OrderedProductRepository;
import com.ordersmicroservice.orders_microservice.services.OrderedProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderedProductsServiceImpl implements OrderedProductService {
    OrderedProductRepository orderedProductRepository;

    public OrderedProductsServiceImpl(OrderedProductRepository orderedProductRepository) {
        this.orderedProductRepository = orderedProductRepository;
    }


    @Override
    public List<OrderedProductEntity> getAllProductsFromOrder() {
        return orderedProductRepository.findAll();
    }
}

