package com.ordersmicroservice.orders_microservice;

import com.ordersmicroservice.orders_microservice.dto.Order;
import com.ordersmicroservice.orders_microservice.models.OrderEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.ordersmicroservice.orders_microservice.dto.Status.PAID;
import static com.ordersmicroservice.orders_microservice.dto.Status.UNPAID;

public class Datos {

    public static Optional<OrderEntity> crearOrder001(){
        return Optional.of(new OrderEntity(1L,1L,"Valencia","Barcelona",PAID, "2001-21-21","2002-21-21"));
    }
    public static Optional<OrderEntity> crearOrder002() {
        return Optional.of(new OrderEntity(2L, 2L, "Valencia", "Barcelona", UNPAID, "2001-21-21","2002-21-21"));
    }
}
