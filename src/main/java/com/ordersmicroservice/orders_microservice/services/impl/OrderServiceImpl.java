package com.ordersmicroservice.orders_microservice.services.impl;


import com.ordersmicroservice.orders_microservice.dto.CreditCardDto;
import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.dto.CartProductDto;
import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.dto.UpdateStockRequest;
import com.ordersmicroservice.orders_microservice.exception.EmptyCartException;
import com.ordersmicroservice.orders_microservice.exception.NotFoundException;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.CartService;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    Random random;
    private final CartService cartService;
    private final RestClient restClient;

    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService, RestClient restClient) {

        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.restClient = restClient;
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
                    .toList();

            order.setUserId(cart.getUserId());
            order.setCartId(id);
            order.setTotalPrice(cart.getTotalPrice());
            order.setOrderedProducts(orderedProducts);
            order.setFromAddress(randomAddress());
            order.setStatus(Status.PAID);
            order.setDateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            cartService.emptyCartProductsById(id);
            return orderRepository.save(order);
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

        Map<Status, Consumer<Order>> statusActions = Map.of(
                Status.DELIVERED, this::handleDeliveredStatus,
                Status.RETURNED, this::handleReturnedStatus
                //Aqui podemos controlar mas status si hiciera falta y hacer un metodo para cada uno
        );

        statusActions.getOrDefault(updatedStatus, order -> {
            throw new IllegalStateException("Unexpected status: " + updatedStatus);
        }).accept(existingOrder);

        return orderRepository.save(existingOrder);
    }

    private void handleDeliveredStatus(Order order) {
        order.setDateDelivered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void handleReturnedStatus(Order order) {
        List<UpdateStockRequest> updateStockRequests = order.getOrderedProducts().stream()
                .map(product -> new UpdateStockRequest(product.getProductId(), product.getQuantity()))
                .toList();

        String url = "http://localhost:8081/catalog/products/";
        updateStockRequests.forEach(request -> restClient.patch().uri(url + request.getProductId() +"/stock?newStock=" + request.getQuantity()).retrieve().body(UpdateStockRequest.class));
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


