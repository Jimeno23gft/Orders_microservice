package com.ordersmicroservice.orders_microservice.services.impl;
import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.services.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Optional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {


    public String cartUri = "/carts/";


    private final RestClient restClient;

    public CartServiceImpl(RestClient.Builder restClient) {
        this.restClient = restClient.build();
    }


    public Optional<CartDto> getCartById(Long id){
        log.info("CartServiceImpl: getCartById ( id = " + id + " )");
        return Optional.ofNullable(restClient.get()
                .uri(cartUri + id)
                .retrieve()
                .body(CartDto.class));
    }

    public void emptyCartProductsById(Long id){
        log.info("CartServiceImpl: emptyCartProductsById ( id = " + id + " )");
        restClient.delete()
                .uri(cartUri + id)
                .retrieve()
                .body(Void.class);
    }
}


