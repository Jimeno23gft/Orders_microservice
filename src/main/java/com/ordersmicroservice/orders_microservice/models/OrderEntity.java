package com.ordersmicroservice.orders_microservice.models;

import com.ordersmicroservice.orders_microservice.dto.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name="from_address")
    private String from_address;

    @Column(name="to_address")
    private String to_address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "date_ordered")
    private String date_ordered;

    @Column(name = "date_delivered")
    private String date_delivered;
}
