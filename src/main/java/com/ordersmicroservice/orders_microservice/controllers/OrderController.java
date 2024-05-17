package com.ordersmicroservice.orders_microservice.controllers;

import com.ordersmicroservice.orders_microservice.dto.StatusUpdateDto;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "List all Orders", description = "This endpoint retrieves example data from the server.")
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Order by ID", description = "This endpoint retrieves example data by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
            Order order = orderService.getOrderById(id);
            return ResponseEntity.ok(order);
    }
    @PostMapping("/{id}")
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a new order", description = "This endpoint retrieves example data from the server.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order Created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public Order postOrder(@PathVariable Long id){ return orderService.addOrder(id);}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel an order", description = "This endpoint retrieves example data from the server.")
    public void deleteById(@PathVariable Long id) {orderService.deleteById(id);}

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an order", description = "This endpoint updates the status of an order based on the provided ID.")
    public ResponseEntity<Order> patchOrder(@PathVariable Long id, @RequestBody StatusUpdateDto patchData) {
        Order updatedOrder = orderService.patchOrder(id, patchData.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }



}
