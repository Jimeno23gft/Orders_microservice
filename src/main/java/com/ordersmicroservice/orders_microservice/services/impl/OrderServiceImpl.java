package com.ordersmicroservice.orders_microservice.services.impl;


import com.ordersmicroservice.orders_microservice.dto.CreditCardDto;
import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.dto.CartProductDto;
import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.exception.EmptyCartException;
import com.ordersmicroservice.orders_microservice.exception.NotFoundException;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.CartService;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    Random random;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService) {

        this.cartService = cartService;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return Optional.of(orderRepository.findAll()).filter(orders -> !orders.isEmpty())
                .orElseThrow(() -> new NotFoundException("No orders were found"));
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));
    }

    @Override
    public Order addOrder(Long id, CreditCardDto creditCard) {
        //log.info("Sending credit card info to payment Server...")
        //log.info("Payment with the credit card " + creditCard.getNumber() + " has been made successfully" )

        CartDto cart;
        try {
            cart = cartService.getCartById(id);
        }catch(Exception ex){
            throw new NotFoundException("Cart not found with ID: " + id);
        }

        if(cart.getCartProducts().isEmpty()){
            throw new EmptyCartException("Empty cart, order not made");
        }

        Order order = new Order();

            List<CartProductDto> cartProducts = cartService.getCartById(id).getCartProducts();

            List<OrderedProduct> orderedProducts = cartProducts.stream()
                    .map(cartProductDto -> convertToOrderedProduct(cartProductDto, order))
                    .collect(Collectors.toList());

            order.setUserId(cart.getUserId());
            order.setCartId(id);
            order.setTotalPrice(cart.getTotalPrice());
            order.setOrderedProducts(orderedProducts);
            order.setFromAddress(randomAddress());
            order.setStatus(Status.PAID);
            order.setDateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            cartService.emptyCartProductsById(id);
            return orderRepository.save(order);


//         order = Order.builder()
//                                                    .userId(1L)
//                                                    .cartId(id)
//                                                    .totalPrice(cart.getTotalPrice())
//                                                    .orderedProducts(orderedProducts)
//                                                    .fromAddress(randomAddress())
//                                                    .status(Status.UNPAID)
//                                                    .dateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
//                                                    .build();

        //return orderRepository.save(order);
    }
    private OrderedProduct convertToOrderedProduct(CartProductDto cartProductDto, Order order) {
        return OrderedProduct.builder()
                .order(order)
                .productId(cartProductDto.getId())
                .name(cartProductDto.getProductName())
                .category(cartProductDto.getProductCategory())
                .description(cartProductDto.getProductDescription())
                .price(cartProductDto.getPrice())
                .quantity(cartProductDto.getQuantity())
                .build();
    }
    private String randomAddress() {
        String[] addresses = {"123 Main St", "456 Elm St", "789 Oak St", "101 Maple Ave", "222 Pine St", "333 Cedar Rd"};
        this.random = new Random();
        return addresses[this.random.nextInt(addresses.length)];
    }

    @Override
    public Order patchOrder(Long id, @RequestBody Status updatedStatus) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + id));
        existingOrder.setStatus(updatedStatus);
        Optional.of(updatedStatus)
                .filter(status -> status == Status.DELIVERED)
                .ifPresent(status -> existingOrder.setDateDelivered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.findById(id)
                .ifPresentOrElse(
                        order -> orderRepository.deleteById(id),
                        () -> {
                            throw new NotFoundException("Order with ID " + id + " not found.");
                        }
                );
    }
}


