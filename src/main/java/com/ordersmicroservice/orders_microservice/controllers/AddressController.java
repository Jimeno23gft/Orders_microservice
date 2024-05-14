package com.ordersmicroservice.orders_microservice.controllers;

import com.ordersmicroservice.orders_microservice.models.Address;
import com.ordersmicroservice.orders_microservice.services.AddressService;
import com.ordersmicroservice.orders_microservice.services.impl.AddressServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders/address")
public class AddressController {
    AddressServiceImpl addressService;

    public AddressController(AddressServiceImpl addressService){
        this.addressService = addressService;
    }

    @GetMapping("/{order_id}")
    public Address getToAddressFromOrder(@PathVariable("order_id") Long orderId){
        return addressService.getToAddressFromOrder(orderId);
    }
}
