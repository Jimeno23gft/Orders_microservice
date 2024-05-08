package com.ordersmicroservice.orders_microservice;

import com.ordersmicroservice.orders_microservice.dto.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class Datos {

    public static Optional<Order> crearOrder001(){
        return Optional.of(new Order(1L,1L,"Valencia","Barcelona","PAID", "2001-21-21","2002-21-21"));
    }
    public static Optional<Order> crearOrder002() {
        return Optional.of(new Order(2L, 2L, "Valencia", "Barcelona", "UNPAID", "2001-21-21","2002-21-21"));
    }
}
