package com.ordersmicroservice.orders_microservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@Generated
public class ErrorMessage {
    private HttpStatus status;
    private String message;
    private Date timestamp;
}
