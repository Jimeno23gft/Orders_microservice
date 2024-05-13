package com.ordersmicroservice.orders_microservice.repositories;

import com.ordersmicroservice.orders_microservice.dto.Order;
import com.ordersmicroservice.orders_microservice.models.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

}
