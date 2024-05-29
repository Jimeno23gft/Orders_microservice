package com.ordersmicroservice.orders_microservice.exception;

public class RetryFailedConnection extends RuntimeException
{
    public RetryFailedConnection(String message) {
        super(message);
    }
}
