package com.ordersmicroservice.orders_microservice.models;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name = "user_id")
    private String user_id;

    @Column(name="from_address")
    private String from_address;

    @Column(name="to_address")
    private String to_address;
    public enum Status {UNPAID, PAID, SENT, IN_DELIVERY, DELIVERED, UNKNOWN};
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "date_ordered")
    private LocalDateTime date_ordered;

    @Column(name = "date_delivered")
    private LocalDateTime date_delivered;

    public static OrderEntity fromEntity(OrderEntity entity){
        return OrderEntity.builder()
                .id(entity.getId())
                .user_id(entity.getUser_id())
                .from_address(entity.getFrom_address())
                .to_address(entity.getTo_address())
                .status(entity.getStatus())
                .date_ordered(entity.getDate_ordered())
                .date_delivered(entity.getDate_delivered())
                .build();
    }

}
