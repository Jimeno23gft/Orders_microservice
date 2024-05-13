package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.dto.Order;

import com.ordersmicroservice.orders_microservice.models.OrderEntity;
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
    private OrderEntity order1;
    private OrderEntity order2;
    private List<OrderEntity> orders;

    @BeforeEach
    public void setup() {
        order1 = OrderEntity.builder()
                .id(1L)
                .user_id(1L)
                .from_address("a")
                .to_address("b")
                .status(DELIVERED)
                .date_ordered("2024-5-9")
                .date_delivered("2024-5-10").build();
        order2 = OrderEntity.builder()
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

        List<OrderEntity> savedOrders = orderService.getAllOrders();
        assertNotNull(savedOrders);
        assertNotEquals(savedOrders, Collections.emptyList());
        assertEquals(orders, savedOrders);
    }

    @Test
    public void testGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));

        OrderEntity savedOrder = orderService.getOrderById(order1.getId());
        assertNotNull(savedOrder);
        assertEquals(order1, savedOrder);
    }

    @Test
    public void testAddOrder() {
        when(orderRepository.save(order1)).thenReturn(order1);

        OrderEntity savedOrder = orderService.addOrder(order1);

        assertNotNull(savedOrder);
        assertEquals(order1, savedOrder);
    }

    @Test
    public void testPatchOrderIfFound(){
        OrderEntity existingOrder = new OrderEntity();
        existingOrder.setId(order1.getId());
        existingOrder.setStatus(order1.getStatus());
        existingOrder.setFrom_address(order1.getFrom_address());

        OrderEntity updatedOrder = new OrderEntity();
        updatedOrder.setStatus(CANCELLED);
        updatedOrder.setFrom_address("aaa");

        when(orderRepository.findById(order1.getId())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        OrderEntity patchedOrder = orderService.patchOrder(order1.getId(), updatedOrder);

        assertEquals(CANCELLED, patchedOrder.getStatus());
        assertEquals("aaa", patchedOrder.getFrom_address());
        verify(orderRepository, times(1)).findById(order1.getId());
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    public void testPatchOrderIfNotFound(){
        OrderEntity existingOrder = new OrderEntity();
        existingOrder.setId(order1.getId());
        existingOrder.setStatus(order1.getStatus());
        existingOrder.setFrom_address(order1.getFrom_address());

        OrderEntity updatedOrder = new OrderEntity();
        updatedOrder.setStatus(CANCELLED);
        updatedOrder.setFrom_address("aaa");

        when(orderRepository.findById(order1.getId())).thenReturn(Optional.empty());

        String message = "Order not found with id " + order1.getId();
        Exception e = assertThrows(RuntimeException.class, ()-> orderService.patchOrder(order1.getId(), updatedOrder));
        assertTrue(e.getMessage().contains(message));

        verify(orderRepository, times(1)).findById(order1.getId());
        verify(orderRepository, times(0)).save(existingOrder);

    }

    @Test
    void testDeleteById() {
        Long orderId = 1L;

        orderService.deleteById(orderId);

        verify(orderRepository).deleteById(orderId);
    }
}
