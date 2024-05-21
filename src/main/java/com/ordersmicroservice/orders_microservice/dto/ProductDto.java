package com.ordersmicroservice.orders_microservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    @JsonProperty("category_Id")
    private Long categoryId;
    private Double weight;
    @JsonProperty("current_stock")
    private int currentStock;

    @JsonProperty("min_stock")
    private int minStock;

}
