package com.ordersmicroservice.orders_microservice.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private Long user_id;
    private String from_address;
    private String to_address;
    private String status;
    private LocalDateTime date_ordered;
    private LocalDateTime date_delivered;


}
