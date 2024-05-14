package com.ordersmicroservice.orders_microservice.services;

import com.ordersmicroservice.orders_microservice.models.Address;
import com.ordersmicroservice.orders_microservice.models.Order;
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
import static org.assertj.core.api.Assertions.assertThat;
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
                .from_address("Barcelona")
                .to_address(new Address(2L, 1L, "C/ bbbbb", 2, "2B", "Barcelona", "22222"))
                .status(DELIVERED)
                .date_ordered("2024-5-9")
                .date_delivered("2024-5-10").build();
        order2 = Order.builder()
                .id(2L)
                .user_id(2L)
                .from_address("Valencia")
                .to_address(new Address(4L, 4L, "C/ ddddd", 4, "4D", "Dimmsdale", "44444"))
                .status(IN_DELIVERY)
                .date_ordered("2024-5-11")
                .date_delivered("2024-5-12").build();
        orders = List.of(order1, order2);
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> savedOrders = orderService.getAllOrders();
        assertNotNull(savedOrders);
        assertNotEquals(savedOrders, Collections.emptyList());
        assertEquals(orders, savedOrders);
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));

        Order savedOrder = orderService.getOrderById(order1.getId());
        assertNotNull(savedOrder);
        assertEquals(order1, savedOrder);
    }

    @Test
    void testAddOrder() {
        when(orderRepository.save(order1)).thenReturn(order1);

        Order savedOrder = orderService.addOrder(order1);

        assertNotNull(savedOrder);
        assertEquals(order1, savedOrder);
    }

    @Test
    void testPatchOrderIfFound(){
        Order existingOrder = new Order();
        existingOrder.setId(order1.getId());
        existingOrder.setStatus(order1.getStatus());
        existingOrder.setDate_delivered(order1.getDate_delivered());

        Order updatedOrder = new Order();
        updatedOrder.setStatus(CANCELLED);
        updatedOrder.setDate_delivered("2024-5-10");

        when(orderRepository.findById(order1.getId())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        Order patchedOrder = orderService.patchOrder(order1.getId(), updatedOrder);

        assertEquals(CANCELLED, patchedOrder.getStatus());
        assertThat(updatedOrder.getDate_delivered()).isEqualTo(patchedOrder.getDate_delivered());
        verify(orderRepository, times(1)).findById(order1.getId());
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    public void testPatchOrderIfNotFound(){
        Order existingOrder = new Order();
        existingOrder.setId(order1.getId());
        existingOrder.setStatus(order1.getStatus());

        Order updatedOrder = new Order();
        updatedOrder.setStatus(CANCELLED);

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
