package com.ordersmicroservice.orders_microservice.repositories;

import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {
}
