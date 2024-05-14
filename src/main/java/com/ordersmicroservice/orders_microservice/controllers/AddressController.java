package com.ordersmicroservice.orders_microservice.controllers;

import com.ordersmicroservice.orders_microservice.services.AddressService;
import com.ordersmicroservice.orders_microservice.services.impl.AddressServiceImpl;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {
    AddressServiceImpl addressService;

    public AddressController(AddressServiceImpl addressService){
        this.addressService = addressService;
    }

}
