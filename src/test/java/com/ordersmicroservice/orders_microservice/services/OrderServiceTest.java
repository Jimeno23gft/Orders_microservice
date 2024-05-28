package com.ordersmicroservice.orders_microservice.services;
import com.ordersmicroservice.orders_microservice.dto.*;
import com.ordersmicroservice.orders_microservice.dto.CreditCardDto;
import com.ordersmicroservice.orders_microservice.dto.CartDto;
import com.ordersmicroservice.orders_microservice.dto.CartProductDto;
import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.dto.StatusUpdateDto;
import com.ordersmicroservice.orders_microservice.exception.EmptyCartException;
import com.ordersmicroservice.orders_microservice.exception.NotFoundException;
import com.ordersmicroservice.orders_microservice.models.Address;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static com.ordersmicroservice.orders_microservice.dto.Status.IN_DELIVERY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.ordersmicroservice.orders_microservice.Datos.crearOrder001;
import static com.ordersmicroservice.orders_microservice.Datos.crearOrder002;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderServiceImpl orderService;
    @Mock
    CartServiceImpl cartService;
    @Mock
    UserServiceImpl userService;
    @Mock
    CountryServiceImpl countryService;
    @Mock
    RestClient restClient;
    @Mock
    AddressServiceImpl addressService;
    private Order order1;
    private Order order2;
    private List<Order> orders;
    UserDto user1;


    @BeforeEach
    public void setup() {

        this.order1 = crearOrder001().orElseThrow();
        this.order2 = crearOrder002().orElseThrow();
        /*
        order1 = Order.builder()
                .id(1L)
                .userId(1L)
                .cartId(1L)
                .fromAddress("Barcelona")
                .status(DELIVERED)
                .dateOrdered("2024-5-9")
                .dateDelivered("2024-5-10").build();
        order2 = Order.builder()
                .id(2L)
                .userId(2L)
                .cartId(2L)
                .fromAddress("Valencia")
                .status(IN_DELIVERY)
                .dateOrdered("2024-5-11")
                .dateDelivered("2024-5-12").build();

         */


        orders = List.of(order1, order2);
    }



    @Test
    @DisplayName("Testing get all Orders from Repository Method")
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> savedOrders = orderService.getAllOrders();
        assertThat(savedOrders)
                .isNotNull()
                .isNotEqualTo(Collections.emptyList())
                .isEqualTo(orders);
    }

    @Test
    @DisplayName("Testing get an order by id from repository")
    void testGetOrderById() {

         this.user1 = UserDto.builder()
                .id(1L)
                .name("Lorenzo")
                .lastName("Perez")
                .email("perez@gmail.com")
                .phone("123123123")
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));
        when(userService.getUserById(1L)).thenReturn(user1);

        Order savedOrder = orderService.getOrderById(order1.getId());
        assertThat(savedOrder)
                .isNotNull()
                .isEqualTo(order1);
    }

    @Test
    @DisplayName("Testing get all Orders from Repository Method")
    void testGetAllByUserId() {
        Long userId = 1L;
        when(orderRepository.findAllByUserId(userId)).thenReturn(orders);

        List<Order> savedOrders = orderService.getAllByUserId(userId);
        assertThat(savedOrders)
                .isNotNull()
                .isNotEqualTo(Collections.emptyList())
                .isEqualTo(orders);
    }
    @Test
    @DisplayName("Testing Adding a new order with just an id")
    void testAddOrder() {
        String[] addresses = {"123 Main St", "456 Elm St", "789 Oak St", "101 Maple Ave", "222 Pine St", "333 Cedar Rd"};

        CreditCardDto creditCard = new CreditCardDto();
        creditCard.setCardNumber(new BigInteger("1234567812345678"));
        creditCard.setExpirationDate("12/25");
        creditCard.setCvcCode(123);

        Long cartId = 1L;
        Long user_id = 1L;
        BigDecimal totalPrice = new BigDecimal("100.00");
        List<CartProductDto> cartProducts = List.of(
                new CartProductDto(1L, "Product1", "Description1", 2,new BigDecimal("20.00")),
                new CartProductDto(2L, "Product2", "Description2", 1,new BigDecimal("30.00"))
        );
        CartDto cartDto = CartDto.builder()
                .userId(user_id)
                .cartProducts(cartProducts)
                .totalPrice(totalPrice)
                .build();

        Address address = Address.builder()
                .orderId(1L)
                .cityName("Barranquilla")
                .zipCode("46134")
                .street("Calle 69")
                .number(43)
                .door("2")
                .countryId(1L)
                .build();

        CountryDto country = CountryDto.builder()
                .id(1L)
                .name("Colombia")
                .tax(21F)
                .prefix("+57")
                .timeZone("Timezone")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Lorenzo")
                .lastName("Perez")
                .email("perez@gmail.com")
                .phone("123123123")
                .address(address)
                .country(country)
                .build();


        when(cartService.getCartById(cartId)).thenReturn(Optional.ofNullable(cartDto));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(userService.getUserById(cartDto.getUserId())).thenReturn(userDto);

        when(countryService.getCountryById(address.getCountryId())).thenReturn(country);

        Order savedOrder = orderService.addOrder(cartId,creditCard);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(Arrays.asList(addresses)).contains(savedOrder.getFromAddress());
        assertThat(savedOrder.getStatus()).isEqualTo(Status.PAID);
        assertThat(savedOrder.getDateOrdered()).isNotNull();
        assertThat(savedOrder.getDateDelivered()).isNull();


        List<OrderedProduct> orderedProducts = savedOrder.getOrderedProducts();
        assertThat(orderedProducts).isNotNull().hasSameSizeAs(cartProducts);

        for (int i = 0; i < cartProducts.size(); i++) {
            CartProductDto cartProduct = cartProducts.get(i);
            OrderedProduct orderedProduct = orderedProducts.get(i);


            assertThat(orderedProduct.getProductId()).isEqualTo(cartProduct.getId());
            assertThat(orderedProduct.getName()).isEqualTo(cartProduct.getProductName());
            assertThat(orderedProduct.getDescription()).isEqualTo(cartProduct.getProductDescription());
            assertThat(orderedProduct.getPrice()).isEqualTo(cartProduct.getPrice());
            assertThat(orderedProduct.getQuantity()).isEqualTo(cartProduct.getQuantity());

        }
    }

    @Test
    @DisplayName("Test addOrder when cart is not found")
    void testAddOrderCartNotFound() {
        Long cartId = 1L;
        when(cartService.getCartById(cartId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.addOrder(cartId, new CreditCardDto()))
                .isInstanceOf(NotFoundException.class);

        verify(cartService, times(1)).getCartById(cartId);
    }

    @Test
    @DisplayName("Test addOrder when cart is empty")
    void testAddOrderEmptyCart() {
        Long cartId = 1L;
        CartDto emptyCart = new CartDto();
        emptyCart.setCartProducts(Collections.emptyList());
        when(cartService.getCartById(cartId)).thenReturn(Optional.of(emptyCart));

        assertThatThrownBy(() -> orderService.addOrder(cartId, new CreditCardDto()))
                .isInstanceOf(EmptyCartException.class);

        verify(cartService, times(1)).getCartById(cartId);
    }

    @Test
    @DisplayName("Testing the update of an order")
    void testPatchOrderIfFound() {
        Order existingOrder = new Order();
        existingOrder.setId(order1.getId());
        existingOrder.setStatus(order1.getStatus());
        existingOrder.setDateDelivered(order1.getDateDelivered());

        StatusUpdateDto statusUpdateDto = new StatusUpdateDto();
        statusUpdateDto.setStatus(Status.CANCELLED);

        when(orderRepository.findById(order1.getId())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        Order patchedOrder = orderService.patchOrder(order1.getId(), statusUpdateDto.getStatus());

        assertThat(patchedOrder.getStatus()).isEqualTo(Status.CANCELLED);

        verify(orderRepository, times(1)).findById(order1.getId());
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    @DisplayName("Testing patching an order with DELIVERED status")
    void testPatchOrderDelivered() {
        Order initialOrder = new Order();
        initialOrder.setId(1L);
        initialOrder.setStatus(IN_DELIVERY);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(initialOrder));
        when(orderRepository.save(initialOrder)).thenReturn(initialOrder);

        Order patchedOrder = orderService.patchOrder(1L, Status.DELIVERED);

        assertThat(patchedOrder).isNotNull();
        assertThat(patchedOrder.getStatus()).isEqualTo(Status.DELIVERED);
        assertThat(patchedOrder.getDateDelivered()).isNotNull();
    }

    @Test
    @DisplayName("Testing the update when order is not found")
    void testPatchOrderIfNotFound() {
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto();
        statusUpdateDto.setStatus(Status.CANCELLED);

        when(orderRepository.findById(order1.getId())).thenReturn(Optional.empty());

        String message = "Order not found with ID: " + order1.getId();

        Status status = statusUpdateDto.getStatus();
        Long order1Id = order1.getId();

        assertThatThrownBy(() -> orderService.patchOrder(order1Id, status))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(message);

        verify(orderRepository, times(1)).findById(order1.getId());
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    @DisplayName("Testing patching an order with RETURNED status")
    void testPatchOrderReturned() {
        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        Order initialOrder = new Order();
        OrderedProduct product1 = new OrderedProduct();
        OrderedProduct product2 = new OrderedProduct();
        product1.setProductId(1L);
        product2.setProductId(2L);
        product1.setQuantity(5);
        product2.setQuantity(3);
        initialOrder.setId(1L);
        initialOrder.setStatus(IN_DELIVERY);
        initialOrder.setOrderedProducts(Arrays.asList(
                product1,
                product2
        ));

        when(restClient.patch()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(initialOrder));
        when(orderRepository.save(initialOrder)).thenReturn(initialOrder);

        Order patchedOrder = orderService.patchOrder(1L, Status.RETURNED);

        assertThat(patchedOrder).isNotNull();
        assertThat(patchedOrder.getStatus()).isEqualTo(Status.RETURNED);

        verify(restClient, times(initialOrder.getOrderedProducts().size())).patch();
    }

    @Test
    @DisplayName("Testing the deleting of an order")
    void testDeleteById() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(new Order()));

        orderService.deleteById(orderId);

        verify(orderRepository).findById(orderId);
        verify(orderRepository).deleteById(orderId);
    }

    @Test
    @DisplayName("Testing the deleting of an order if the order with the given id is not found")
    void testDeleteByIdNotFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.deleteById(orderId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Order with ID " + orderId + " not found.");

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).deleteById(orderId);
    }
}
