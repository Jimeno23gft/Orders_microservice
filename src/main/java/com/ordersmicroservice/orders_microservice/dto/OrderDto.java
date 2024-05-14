package com.ordersmicroservice.orders_microservice.dto;

import com.ordersmicroservice.orders_microservice.models.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    @Id
    private Long id;
    private Long user_id;
    private Address from_address;
    private Address to_address;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String date_ordered;
    private String date_delivered;
}
