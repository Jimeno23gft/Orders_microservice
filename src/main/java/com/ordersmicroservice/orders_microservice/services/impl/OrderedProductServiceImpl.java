package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.repositories.OrderedProductRepository;
import com.ordersmicroservice.orders_microservice.services.OrderedProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderedProductServiceImpl implements OrderedProductService {

    OrderedProductRepository orderedProductRepository;

    public OrderedProductServiceImpl(OrderedProductRepository orderedProductRepository){
        this.orderedProductRepository = orderedProductRepository;
    }

    @Override
    public List<OrderedProduct> getAllProductsFromOrder(Long orderId) {
        log.info("obteniendo todos los productos del pedido con Id: {} ", orderId);
        return orderedProductRepository.findByOrderId(orderId);
    }
}
