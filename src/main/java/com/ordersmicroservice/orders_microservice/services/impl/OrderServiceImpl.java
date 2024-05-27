package com.ordersmicroservice.orders_microservice.services.impl;

import com.ordersmicroservice.orders_microservice.dto.*;
import com.ordersmicroservice.orders_microservice.dto.CreditCardDto;
import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.dto.CartProductDto;
import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.dto.UpdateStockRequest;
import com.ordersmicroservice.orders_microservice.exception.EmptyCartException;
import com.ordersmicroservice.orders_microservice.exception.NotFoundException;
import com.ordersmicroservice.orders_microservice.models.Address;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
        return Optional.of(orderRepository.findAll()).filter(orders -> !orders.isEmpty())
                .orElseThrow(() -> new NotFoundException("No orders were found"));
    }

    @Override
    public Order getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));
        UserDto user = userService.getUserById(order.getUserId());
        UserResponseDto userResponde = UserResponseDto.fromUserDto(user);
        order.setUser(userResponde);

        return order;
    }

    @Override
    public List<Order> getAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }


    @Override
    public Order addOrder(Long cartId, CreditCardDto creditCard) {
        //log.info("Sending credit card info to payment Server...")
        //log.info("Payment with the credit card " + creditCard.getNumber() + " has been made successfully" )
        UserDto user;
        Address address;
        CountryDto country;
        UserResponseDto userResponse = new UserResponseDto();
        CartDto cart;

        try {
            cart = cartService.getCartById(cartId);
        } catch (Exception ex) {
            throw new NotFoundException("Cart not found with ID: " + cartId);
        }

        if (cart.getCartProducts().isEmpty()) {
            throw new EmptyCartException("Empty cart, order not made");
        }

        List<CartProductDto> cartProducts = cartService.getCartById(cartId).getCartProducts();

        Order order = new Order();
        List<OrderedProduct> orderedProducts = cartProducts.stream()
                .map(cartProductDto -> convertToOrderedProduct(cartProductDto, order))
                .collect(Collectors.toList());


        System.out.println(cart.getId());
        System.out.println(cart.getUserId());
        System.out.println(cart.getTotalPrice());
        System.out.println(cart);

        try {
            user = userService.getUserById(cart.getUserId());
        } catch (Exception ex) {
            throw new NotFoundException("User not found with ID: " + cartId);
        }

        System.out.println(user);

        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        userResponse.setLastName(user.getLastName());

        System.out.println(userResponse);

        order.setCartId(cart.getId());
        System.out.println(cart.getId());
        order.setUserId(cart.getUserId());
        System.out.println(cart.getUserId());
        order.setFromAddress(randomAddress());
        System.out.println(order.getFromAddress());
        order.setStatus(Status.PAID);
        System.out.println(order.getStatus());
        order.setDateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println(order.getDateOrdered());
        order.setUser(userResponse);
        System.out.println(order.getUser());
        order.setOrderedProducts(orderedProducts);
        System.out.println(order.getOrderedProducts());
        order.setTotalPrice(cart.getTotalPrice());
        System.out.println(order.getTotalPrice());
        System.out.println(order);
        try {
            country = countryService.getCountryById(user.getCountry().getId());
        } catch (NotFoundException ex) {
            throw new NotFoundException("Country not found with ID: " + user.getCountry().getId());
        }

        address = user.getAddress();
        address.setCountryId(user.getCountry().getId());
        order.setCountry(country);
        address.setOrder(order);
        addressService.saveAddress(address);
        order.setAddress(address);
        cartService.emptyCartProductsById(cartId);

        System.out.println(order);

        return orderRepository.save(order);
    }

    private OrderedProduct convertToOrderedProduct(CartProductDto cartProductDto, Order order) {
        return OrderedProduct.builder()
                .order(order)
                .productId(cartProductDto.getId())
                .name(cartProductDto.getProductName())
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

            //Aqui si en un futuro queremos, podemos hacer que si el status que nos mandan no coincide con ninguno de los del map lance una excepcion
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

        //String url = "https://catalog-workshop-yequy5sv5a-uc.a.run.app/catalog/products/";
        String url = "http://localhost:8083/catalog/products/";

        updateStockRequests.forEach(request -> restClient.patch()
                .uri(url + request.getProductId() + "/stock?newStock=" + request.getQuantity()).retrieve().body(UpdateStockRequest.class));
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


