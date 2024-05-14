package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.repositories.OrderedProductRepository;
import com.ordersmicroservice.orders_microservice.services.OrderedProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderedProductServiceImpl implements OrderedProductService {
    OrderedProductRepository orderedProductRepository;
    public OrderedProductServiceImpl(OrderedProductRepository orderedProductRepository){
        this.orderedProductRepository = orderedProductRepository;
    }
    @Override
    public List<OrderedProduct> getAllProductsFromOrder(Long orderId) {
        return orderedProductRepository.findByOrderId(orderId);
    }
}
