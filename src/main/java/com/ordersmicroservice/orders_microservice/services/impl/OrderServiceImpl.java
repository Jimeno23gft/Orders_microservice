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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

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
        CountryDto countryDto = countryService.getCountryById(order.getCountryId());
        order.setCountry(countryDto);
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

        CartDto cart = cartService.getCartById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found with ID: " + cartId));

        if (cart.getCartProducts().isEmpty()) {
            throw new EmptyCartException("Empty cart, order not made");
        }

        List<CartProductDto> cartProducts = cart.getCartProducts();

        Order order = new Order();
        List<OrderedProduct> orderedProducts = new ArrayList<>(cartProducts.stream()
                .map(cartProductDto -> convertToOrderedProduct(cartProductDto, order))
                .toList());
       try {
            user = userService.getUserById(cart.getUserId());
        } catch (Exception ex) {
            throw new NotFoundException("User not found with ID: " + cartId);
        }

        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());

        order.setCartId(cart.getId());
        order.setUserId(cart.getUserId());
        order.setFromAddress(randomAddress());
        order.setStatus(Status.PAID);
        order.setDateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        order.setUser(userResponse);
        order.setOrderedProducts(orderedProducts);
        order.setTotalPrice(cart.getTotalPrice());

        try {
            country = countryService.getCountryById(user.getCountry().getId());
        } catch (NotFoundException ex) {
            throw new NotFoundException("Country not found with ID: " + user.getCountry().getId());
        }

        System.out.println(country);

        address = user.getAddress();
        address.setCountryId(user.getCountry().getId());
        address.setOrder(order);
        order.setCountryId(user.getCountry().getId());
        order.setCountry(country);
        order.setAddress(address);
        addressService.saveAddress(address);
        cartService.emptyCartProductsById(cartId);

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
    @Transactional
    public Order patchOrder(Long id, @RequestBody Status updatedStatus) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + id));
        existingOrder.setStatus(updatedStatus);

        Map<Status, Consumer<Order>> statusActions = Map.of(
                Status.DELIVERED, this::handleDeliveredStatus,
                Status.RETURNED, this::handleReturnedStatus
                //TODO: Aqui podemos controlar mas status si hiciera falta y hacer un metodo para cada uno
        );

        statusActions.getOrDefault(updatedStatus, order -> {

            //TODO: Aqui si en un futuro queremos, podemos hacer que si el status que nos mandan no coincide con ninguno de los del map lance una excepcion
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

        //String url = "https://catalog-workshop-yequy5sv5a-uc.a.run.app/catalog/products/"
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


