package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.Order;

import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ordersmicroservice.orders_microservice.dto.Status.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderEntityServiceTest {
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
    public void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> savedOrders = orderService.getAllOrders();
        assertNotNull(savedOrders);
        assertNotEquals(savedOrders, Collections.emptyList());
        assertEquals(orders, savedOrders);
    }

    @Test
    public void testGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));

        Order savedOrder = orderService.getOrderById(order1.getId());
        assertNotNull(savedOrder);
        assertEquals(order1, savedOrder);
    }

    @Test
    public void testAddOrder() {
        when(orderRepository.save(order1)).thenReturn(order1);

        Order savedOrder = orderService.addOrder(order1);

        assertNotNull(savedOrder);
        assertEquals(order1, savedOrder);
    }

    @Test
    public void testPatchOrderIfFound(){
        Order existingOrder = new Order();
        existingOrder.setId(order1.getId());
        existingOrder.setStatus(order1.getStatus());
        existingOrder.setFrom_address(order1.getFrom_address());

        Order updatedOrder = new Order();
        updatedOrder.setStatus(CANCELLED);
        updatedOrder.setFrom_address("aaa");

        // Mocking the behavior of orderRepository
        when(orderRepository.findById(order1.getId())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        // Call the method
        Order patchedOrder = orderService.patchOrder(order1.getId(), updatedOrder);

        // Assertions
        assertEquals(CANCELLED, patchedOrder.getStatus()); // Ensure the status is updated
        assertEquals("aaa", patchedOrder.getFrom_address()); // Ensure the status is updated
        verify(orderRepository, times(1)).findById(order1.getId()); // Ensure findById is called exactly once with orderId
        verify(orderRepository, times(1)).save(existingOrder); // Ensure save is called exactly once with existingOrder
    }

    @Test
    public void testPatchOrderIfNotFound(){
        Order existingOrder = new Order();
        existingOrder.setId(order1.getId());
        existingOrder.setStatus(order1.getStatus());
        existingOrder.setFrom_address(order1.getFrom_address());

        Order updatedOrder = new Order();
        updatedOrder.setStatus(CANCELLED);
        updatedOrder.setFrom_address("aaa");

        // Mocking the behavior of orderRepository
        when(orderRepository.findById(order1.getId())).thenReturn(Optional.empty());

        // Call the method
        //Order patchedOrder = orderService.patchOrder(order1.getId(), updatedOrder);
        String message = "Order not found with id " + order1.getId();
        Exception e = assertThrows(RuntimeException.class, ()-> orderService.patchOrder(order1.getId(), updatedOrder));
        assertTrue(e.getMessage().contains(message));
        // Assertions
        //assertNull(patchedOrder.getStatus()); // Ensure the status is updated
        verify(orderRepository, times(1)).findById(order1.getId()); // Ensure findById is called exactly once with orderId
        verify(orderRepository, times(0)).save(existingOrder); // Ensure save is called exactly once with existingOrder

    }

    @Test
    void testDeleteById() {

    }
}
