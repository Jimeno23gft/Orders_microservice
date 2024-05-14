package com.ordersmicroservice.orders_microservice.controllers;

import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.services.OrderedProductService;
import com.ordersmicroservice.orders_microservice.services.impl.OrderedProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/orders")
public class OrderedProductController {
    OrderedProductServiceImpl orderedProductService;
    public OrderedProductController(OrderedProductServiceImpl orderedProductService){
        this.orderedProductService = orderedProductService;
    }

    @GetMapping("/{orderId}/products")
    @ResponseStatus(OK)
    @Operation(summary = "List all products from an order", description = "This endpoint retrieves example data from the server.")
    public List<OrderedProduct> getAllProductsFromOrder(@PathVariable("orderId") Long orderId){
        return orderedProductService.getAllProductsFromOrder(orderId);
    }
}
