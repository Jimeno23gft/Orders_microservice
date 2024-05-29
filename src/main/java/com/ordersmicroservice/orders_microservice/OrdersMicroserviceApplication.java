package com.ordersmicroservice.orders_microservice;
import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@Generated
@EnableRetry
public class OrdersMicroserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrdersMicroserviceApplication.class, args);
	}
}
