package com.ordersmicroservice.orders_microservice;

import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import lombok.Generated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ordersmicroservice.orders_microservice.dto.Status.PAID;
import static com.ordersmicroservice.orders_microservice.dto.Status.UNPAID;

@Generated
public class Datos {

    static List<OrderedProduct> productList = new ArrayList<>();


    public static Optional<Order> crearOrder001(){
        return Optional.of(new Order(1L,1L,"Valencia","Barcelona",PAID, "2001-21-21","2002-21-21",new BigDecimal("15"),productList));
    }
    public static Optional<Order> crearOrder002() {
        return Optional.of(new Order(2L, 2L, "Valencia", "Barcelona", UNPAID, "2001-21-21","2002-21-21",new BigDecimal("15"),productList));
    }
    public static void main(String[] args) {
        productList.add(new OrderedProduct());
        productList.add(new OrderedProduct());
        productList.add(new OrderedProduct());
    }
}
