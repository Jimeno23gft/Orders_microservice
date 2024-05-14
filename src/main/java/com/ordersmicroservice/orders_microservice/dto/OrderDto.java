package com.ordersmicroservice.orders_microservice.dto;

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
    private String from_address;
    private String to_address;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String date_ordered;
    private String date_delivered;

}
