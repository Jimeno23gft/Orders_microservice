package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.exception.GlobalExceptionHandler;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ordersmicroservice.orders_microservice.dto.Status.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderServiceImpl orderService;
    private Order order1;
    private Order order2;
    private List<Order> orders;

    @BeforeEach
    public void setup() {
        order1 = Order.builder()
                .id(1L)
                .user_id(1L)
                .from_address("a")
                .to_address("b")
                .status(DELIVERED)
                .date_ordered("2024-5-9")
                .date_delivered("2024-5-10").build();
        order2 = Order.builder()
                .id(2L)
                .user_id(2L)
                .from_address("c")
                .to_address("d")
                .status(IN_DELIVERY)
                .date_ordered("2024-5-11")
                .date_delivered("2024-5-12").build();
        orders = List.of(order1, order2);
    }

    @Test
    @DisplayName("Testing get all Orders from Repository Method")
    public void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> savedOrders = orderService.getAllOrders();
        assertNotNull(savedOrders);
        assertNotEquals(savedOrders, Collections.emptyList());
        assertEquals(orders, savedOrders);
    }

    @Test
    @DisplayName("Testing get an order by id from repository")
    public void testGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));

        Order savedOrder = orderService.getOrderById(order1.getId());
        assertNotNull(savedOrder);
        assertEquals(order1, savedOrder);
    }

    @Test
    @DisplayName("Testing Adding a new order with just an id")
    public void testAddOrder() {
        String[] addresses = {"123 Main St","456 Elm St","789 Oak St","101 Maple Ave","222 Pine St","333 Cedar Rd"};

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Order savedOrder = orderService.addOrder(1L);

        assertNotNull(savedOrder);
        assertEquals(1L, savedOrder.getUser_id());
        assertTrue(Arrays.asList(addresses).contains( savedOrder.getFrom_address()));
        assertEquals(Status.UNPAID, savedOrder.getStatus());
        assertNotNull(savedOrder.getDate_ordered());
        assertNull(savedOrder.getDate_delivered());
    }

    @Test
    @DisplayName("Testing the update of an order")
    public void testPatchOrderIfFound(){
        Order existingOrder = new Order();
        existingOrder.setId(order1.getId());
        existingOrder.setStatus(order1.getStatus());

        Order updatedOrder = new Order();
        updatedOrder.setStatus(CANCELLED);

        when(orderRepository.findById(order1.getId())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        Order patchedOrder = orderService.patchOrder(order1.getId(), updatedOrder);

        assertEquals(CANCELLED, patchedOrder.getStatus());
        verify(orderRepository, times(1)).findById(order1.getId());
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    @DisplayName("Testing the update when order is not found")
    public void testPatchOrderIfNotFound(){
        Long orderId = 1L;
        Order updatedOrder = new Order();
        updatedOrder.setStatus(Status.DELIVERED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        GlobalExceptionHandler.NotFoundException exception = assertThrows(GlobalExceptionHandler.NotFoundException.class, () -> {
            orderService.patchOrder(orderId, updatedOrder);
        });
        assertTrue(exception.getMessage().contains("Order not found with id: " + orderId));

    }

    @Test
    @DisplayName("Testing the deleting of an order")
    void testDeleteById() {
        Long orderId = 1L;

        orderService.deleteById(orderId);

        verify(orderRepository).deleteById(orderId);
    }
}
