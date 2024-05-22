package com.ordersmicroservice.orders_microservice.controllers;
import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.dto.CartProductDto;
import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.models.Address;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.services.CartService;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@WebFluxTest(OrderController.class)
public class OrderControllerIntegrationTest {



    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CartService cartService;

    @Test
    public void testAddOrderFromCartData() {

        Address address = new Address();
       CartProductDto cartProductDto = new CartProductDto(1005L, "Product5", 1, BigDecimal.valueOf(70);
       List<CartProductDto> cartProductDtoList = new ArrayList<>();


        CartDto cartDto = new CartDto(1003L, 3L, 1L, cartProductDtoList, new BigDecimal("17"));  // Uso correcto de comillas para String
        // Simulando la obtención de datos de carrito
        when(cartService.getCartById(any(Long.class))).thenReturn(cartDto);

        // Preparar el objeto Order que será devuelto por el método addOrder
        Order expectedOrder = Order.builder()
                .id(3L)
                .userId(3L)
                .cartId(1003L)
                .fromAddress("789 Oak St")
                .status(Status.IN_DELIVERY)
                .dateOrdered("2024-05-09 10:00:00")
                .dateDelivered("2024-05-11 17:00:00")
                .address(3L, "Oak Street", 789, "C", "Capital City", "10112", 3L)
                .totalPrice(BigDecimal.valueOf(17))
                .orderedProducts(Arrays.asList(
                        new OrderedProduct(3L, 1005L, "Product5", "Category5", "Description5", 70L, 1)
                ))
                .build();

        when(orderService.addOrder(any(Long.class))).thenReturn(expectedOrder);

        // Realizar la petición POST y verificar la respuesta
        webTestClient.post().uri("/orders/1003")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(new Object()))  // Este Object() debería ser reemplazado por datos reales si es necesario
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.userId").isEqualTo(3)
                .jsonPath("$.cartId").isEqualTo(1003)
                .jsonPath("$.fromAddress").isEqualTo("789 Oak St")
                .jsonPath("$.status").isEqualTo("IN_DELIVERY")
                .jsonPath("$.dateOrdered").isEqualTo("2024-05-09 10:00:00")
                .jsonPath("$.dateDelivered").isEqualTo("2024-05-11 17:00:00")
                .jsonPath("$.address.addressId").isEqualTo(3)
                .jsonPath("$.totalPrice").isEqualTo(17)
                .jsonPath("$.orderedProducts[0].productId").isEqualTo(1005);
    }
    }


