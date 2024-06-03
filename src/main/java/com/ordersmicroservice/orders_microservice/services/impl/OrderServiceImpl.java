package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.*;
import com.ordersmicroservice.orders_microservice.dto.CreditCardDto;
import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.dto.UpdateStockRequest;
import com.ordersmicroservice.orders_microservice.exception.EmptyCartException;
import com.ordersmicroservice.orders_microservice.exception.NotFoundException;
import com.ordersmicroservice.orders_microservice.models.Address;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

import static com.ordersmicroservice.orders_microservice.dto.Status.PAID;

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    Random random;
    CartService cartService;
    UserService userService;
    AddressService addressService;
    CountryService countryService;
    RestClient restClient;


    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService, UserService userService, AddressService addressService, CountryService countryService, RestClient restClient) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.userService = userService;
        this.addressService = addressService;
        this.countryService = countryService;
        this.restClient = restClient;
    }

    @Override
    public List<Order> getAllOrders() {
         List<Order> ordersList = Optional.of(orderRepository.findAll()).filter(orders -> !orders.isEmpty())
                .orElseThrow(() -> new NotFoundException("No orders were found"));
        ordersList.forEach(this::setCountryAndUserToOrder);
        return ordersList;
    }

    @Override
    public Order getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));
        setCountryAndUserToOrder(order);
        return order;
    }
    //Change to stream and not void
    private void setCountryAndUserToOrder(Order order) {
        UserDto user = userService.getUserById(order.getUserId()).orElseThrow(() -> new NotFoundException("User not found with ID: " + order.getUserId()));
        UserResponseDto userResponse = UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
        CountryDto countryDto = countryService.getCountryById(order.getCountryId()).orElseThrow();
        order.setCountry(countryDto);
        order.setUser(userResponse);
    }

    @Override
    public List<Order> getAllByUserId(Long userId) {

        List<Order> ordersList = orderRepository.findAllByUserId(userId);
        ordersList.forEach(this::setCountryAndUserToOrder);
        return ordersList;
    }

    //CreateOrder change name
    @Override
    public Order addOrder(Long cartId, CreditCardDto creditCard) {
        //log.info("Sending credit card info to payment Server...")
        //log.info("Payment with the credit card " + creditCard.getNumber() + " has been made successfully" )



        CartDto cart = checkCartAndCartProducts(cartId);
        List<OrderedProduct> orderedProducts =cart.getCartProducts().stream().map(cartProductDto -> OrderedProduct.builder()
                        .productId(cartProductDto.getId())
                        .name(cartProductDto.getProductName())
                        .description(cartProductDto.getProductDescription())
                        .price(cartProductDto.getPrice())
                        .quantity(cartProductDto.getQuantity())
                        .build())
                .toList();


        UserDto user = getUserFromCart(cart, cartId);
        UserResponseDto userResponse = UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();

<<<<<<< HEAD
        Order order = Order.builder()
                .cartId(cart.getId())
                .userId(cart.getUserId())
                .fromAddress(randomAddress())
                .status(PAID)
                .dateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .user(userResponse)
                .orderedProducts(orderedProducts)
                .totalPrice(cart.getTotalPrice())
                .build();
=======
        //Change to builder
        order.setCartId(cart.getId());
        order.setUserId(cart.getUserId());
        order.setFromAddress(randomAddress());
        order.setStatus(Status.PAID);
        order.setDateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        order.setUser(userResponse);
        order.setOrderedProducts(orderedProducts);
        order.setTotalPrice(cart.getTotalPrice());
>>>>>>> develop

        configureCountryAndAddress(order, user);
        cartService.emptyCartProductsById(cartId);

        updateStockForOrderedProducts(orderedProducts);

        return orderRepository.save(order);
    }
    //Change to builder

    private void updateStockForOrderedProducts(List<OrderedProduct> orderedProducts) {
        List<UpdateStockRequest> updateStockRequests = orderedProducts.stream()
                .map(product -> new UpdateStockRequest(product.getProductId(), product.getQuantity() * (-1)))
                .toList();

        String url = "https://catalog-workshop-yequy5sv5a-uc.a.run.app/catalog/products/newStock/";

        patchOrders(updateStockRequests, url);
    }

    private void patchOrders(List<UpdateStockRequest> updateStockRequests, String url) {
        updateStockRequests.forEach(request -> restClient.patch()
                .uri(url + request.getProductId() + "/quantity?quantity=" + request.getQuantity()));
    }

    private void configureCountryAndAddress(Order order, UserDto user) {

        CountryDto country = countryService.getCountryById(user.getCountry().getId())
                .orElseThrow(() -> new NotFoundException("Country not found with ID: " + user.getCountry().getId()));

        Address address = user.getAddress();
        address.setCountryId(user.getCountry().getId());
        address.setOrder(order);
        order.setCountryId(user.getCountry().getId());
        order.setCountry(country);
        order.setAddress(address);
        addressService.saveAddress(address);
    }

    private UserDto getUserFromCart(CartDto cart, Long cartId) {
        return userService.getUserById(cart.getUserId()).orElseThrow(() -> new NotFoundException("User not found with ID: " + cartId));
    }

    private CartDto checkCartAndCartProducts(Long cartId) {
        CartDto cart = cartService.getCartById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found with ID: " + cartId));

        if (cart.getCartProducts().isEmpty()) {
            throw new EmptyCartException("Empty cart, order not made");
        }
        return cart;
    }

    private String randomAddress() {
        String[] addresses = {"123 Main St", "456 Elm St", "789 Oak St", "101 Maple Ave", "222 Pine St", "333 Cedar Rd"};
        this.random = new Random();
        return addresses[this.random.nextInt(addresses.length)];
    }

    @Override
    @Transactional
    public Order patchOrder(Long id, @RequestBody Status updatedStatus) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + id));
        existingOrder.setStatus(updatedStatus);

        Map<Status, Consumer<Order>> statusActions = Map.of(
                Status.DELIVERED, this::handleDeliveredStatus,
                Status.RETURNED, this::handleReturnedStatus
        );

        statusActions.getOrDefault(updatedStatus, order -> {

        }).accept(existingOrder);

        setCountryAndUserToOrder(existingOrder);
        return orderRepository.save(existingOrder);
    }

    private void handleDeliveredStatus(Order order) {
        order.setDateDelivered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        int points = order.getOrderedProducts().size();
        userService.patchFidelityPoints(order.getUserId(), points);
    }

    private void handleReturnedStatus(Order order) {
        List<UpdateStockRequest> updateStockRequests = order.getOrderedProducts().stream()
                .map(product -> new UpdateStockRequest(product.getProductId(), product.getQuantity()))
                .toList();

        String url = "https://catalog-workshop-yequy5sv5a-uc.a.run.app/catalog/products/";
        //String url = "http://localhost:8083/catalog/products/";

        updateStockRequests.forEach(request -> restClient.patch()
                .uri(url + "/newStock/"+request.getProductId() + "/quantity?quantity=" + request.getQuantity()).retrieve().body(UpdateStockRequest.class));

        int points = order.getOrderedProducts().size();
        userService.patchFidelityPoints(order.getUserId(), -points);
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


