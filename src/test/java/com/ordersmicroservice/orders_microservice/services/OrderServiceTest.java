package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.dto.StatusUpdateDto;
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

import static com.ordersmicroservice.orders_microservice.dto.Status.DELIVERED;
import static com.ordersmicroservice.orders_microservice.dto.Status.IN_DELIVERY;
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
                .cartId(1L)
                .fromAddress("Barcelona")
                .status(DELIVERED)
                .dateOrdered("2024-5-9")
                .dateDelivered("2024-5-10").build();
        order2 = Order.builder()
                .id(2L)
                .cartId(2L)
                .fromAddress("Valencia")
                .status(IN_DELIVERY)
                .dateOrdered("2024-5-11")
                .dateDelivered("2024-5-12").build();
        orders = List.of(order1, order2);
    }

    @Test

    @DisplayName("Testing get all Orders from Repository Method")
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> savedOrders = orderService.getAllOrders();
        assertNotNull(savedOrders);
        assertNotEquals(Collections.emptyList(), savedOrders);
        assertEquals(orders, savedOrders);
    }

    @Test
    @DisplayName("Testing get an order by id from repository")
    void testGetOrderById() {

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));

        Order savedOrder = orderService.getOrderById(order1.getId());
        assertNotNull(savedOrder);
        assertEquals(order1, savedOrder);
    }

    @Test
    @DisplayName("Testing Adding a new order with just an id")
    void testAddOrder() {
        String[] addresses = {"123 Main St", "456 Elm St", "789 Oak St", "101 Maple Ave", "222 Pine St", "333 Cedar Rd"};

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Order savedOrder = orderService.addOrder(1L);

        assertNotNull(savedOrder);
        assertEquals(1L, savedOrder.getCartId());
        assertTrue(Arrays.asList(addresses).contains(savedOrder.getFromAddress()));
        assertEquals(Status.UNPAID, savedOrder.getStatus());
        assertNotNull(savedOrder.getDateOrdered());
        assertNull(savedOrder.getDateDelivered());
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

        assertEquals(Status.CANCELLED, patchedOrder.getStatus());

        verify(orderRepository, times(1)).findById(order1.getId());
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    @DisplayName("Testing patching an order with DELIVERED status")
    void testPatchOrderDelivered() {
        Order initialOrder = new Order();
        initialOrder.setId(1L);
        initialOrder.setStatus(IN_DELIVERY); // Assuming initial status is PENDING

        when(orderRepository.findById(1L)).thenReturn(Optional.of(initialOrder));
        when(orderRepository.save(initialOrder)).thenReturn(initialOrder);

        Order patchedOrder = orderService.patchOrder(1L, Status.DELIVERED);

        assertNotNull(patchedOrder);
        assertEquals(Status.DELIVERED, patchedOrder.getStatus());
        assertNotNull(patchedOrder.getDateDelivered());
        // Add assertions for the date format if needed
    }

    @Test
    @DisplayName("Testing the update when order is not found")
    void testPatchOrderIfNotFound() {
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto();
        statusUpdateDto.setStatus(Status.CANCELLED);

        when(orderRepository.findById(order1.getId())).thenReturn(Optional.empty());

        String message = "Order not found with id " + order1.getId();

        Status status = statusUpdateDto.getStatus();
        Long order1Id = order1.getId();

        Exception e = assertThrows(RuntimeException.class, () -> orderService.patchOrder(order1Id, status));
        assertTrue(e.getMessage().contains(message));

        verify(orderRepository, times(1)).findById(order1.getId());
        verify(orderRepository, times(0)).save(any(Order.class));
    }


    @Test
    @DisplayName("Testing the deleting of an order")
    void testDeleteById() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order1));
        orderService.deleteById(orderId);

        verify(orderRepository).findById(orderId);
        verify(orderRepository).deleteById(orderId);
    }
}
