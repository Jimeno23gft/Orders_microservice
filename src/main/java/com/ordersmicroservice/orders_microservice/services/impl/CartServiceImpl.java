package com.ordersmicroservice.orders_microservice.services.impl;
import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.services.CartService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Optional;


@Service
public class CartServiceImpl implements CartService {


    public static final String CART_URI = "/carts/";


    private final RestClient restClient;

    public CartServiceImpl(RestClient.Builder restClient) {
        this.restClient = restClient.build();
    }


    public Optional<CartDto> getCartById(Long id){
        return Optional.ofNullable(restClient.get()
                .uri(CART_URI + id)
                .retrieve()
                .body(CartDto.class));
    }

    public void emptyCartProductsById(Long id){
        restClient.delete()
                .uri(CART_URI + id)
                .retrieve()
                .body(Void.class);
    }
}


