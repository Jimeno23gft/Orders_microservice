package com.ordersmicroservice.orders_microservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="order_id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "productId")
    private Long productId;

    @Column(name="from_address_id")
    private String fromAddressId;

    @Column(name="to_address_id")
    private String toAddressId;
    public enum Status {UNPAID, PAID, SENT, IN_DELIVERY, DELIVERED, UNKNOWN};
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "date_ordered")
    private LocalDateTime dateOrdered;

    @Column(name = "date_delivered")
    private LocalDateTime dateDelivered;



}
