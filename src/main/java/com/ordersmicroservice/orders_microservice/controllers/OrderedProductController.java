package com.ordersmicroservice.orders_microservice.controllers;

import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.services.OrderedProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/orders/products")
public class OrderedProductController {
    OrderedProductService orderedProductService;
    public OrderedProductController(OrderedProductService orderedProductService){
        this.orderedProductService = orderedProductService;
    }

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "List all products from an order", description = "This endpoint retrieves example data from the server.")
    public List<OrderedProduct> getAllProductsFromOrder(){
        return orderedProductService.getAllProductsFromOrder();
    }
}
