package com.ordersmicroservice.orders_microservice.services.impl;
import com.ordersmicroservice.orders_microservice.dto.Status;
import com.ordersmicroservice.orders_microservice.exception.GlobalExceptionHandler;
import com.ordersmicroservice.orders_microservice.models.Order;
import com.ordersmicroservice.orders_microservice.repositories.OrderRepository;
import com.ordersmicroservice.orders_microservice.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    Random random = new Random();

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {

        log.info("Recuperando todos los pedidos");
        return Optional.of(orderRepository.findAll()).filter(orders -> !orders.isEmpty())
                .orElseThrow(() -> {
                    log.error("No se encontraron pedidos");
                    return new GlobalExceptionHandler.NotFoundException("No orders were found");
                });
    }



    @Override
    public Order getOrderById(Long orderId){
        log.info("Buscando pedido con ID: {}", orderId);
        if (orderId == null || orderId <= 0) {
            log.error("El id introducido {} no es valido (null|<=0)", orderId);
            throw new GlobalExceptionHandler.BadRequest("Invalid id type. Expected type: Long");
        }
        return orderRepository.findById(orderId).orElseThrow(() -> {
            log.error("No se ha encontrado el pedido con id: {}", orderId);
            throw new GlobalExceptionHandler.NotFoundException("Order not found with ID: " + orderId);
        });
    }

    @Override
    public Order addOrder(Long id) {

try{
        log.info("Creando nuevo pedido para el usuario ID: {}", id);

        Order order = new Order();
        order.setUserId(id);
        order.setFromAddress(randomAddress());
        order.setStatus(Status.UNPAID);
        order.setDateOrdered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        Order savedOrder = orderRepository.save(order);
        log.debug("Pedido creado exitosamente con ID: {}", savedOrder.getId());
        log.debug("Detallaes del pedido: {}", order);

        return savedOrder;

    }catch (GlobalExceptionHandler.BadRequest ex){
        //log.error("Error, no se ha creado el pedido para el carrito con ID {}: {}", cart_id, ex.getMessage());
        throw new GlobalExceptionHandler.BadRequest("Error al crear el pedido para el usuario ID " + id + ": " + ex.getMessage());
    }

    }

    private String randomAddress() {
        String[] adresses = {"123 Main St","456 Elm St","789 Oak St","101 Maple Ave","222 Pine St","333 Cedar Rd"};

        return adresses[this.random.nextInt(adresses.length)];
    }

    public Order patchOrder(Long id, @RequestBody Status updatedStatus) {
        try {
            log.info("Actualizando pedido ID: {} con nuevo estado: {}", id, updatedStatus);

            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("El pedido que se quiere actualizar no existe");
                        return new GlobalExceptionHandler.NotFoundException("Order not found with id " + id);
                    });
            Status previousStatus = existingOrder.getStatus();

            existingOrder.setStatus(updatedStatus);
            if (updatedStatus == Status.DELIVERED) {
                existingOrder.setDateDelivered(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }

            log.debug("Pedido ID: {} actualizado de {} a {}", id, previousStatus, updatedStatus);

            return orderRepository.save(existingOrder);
        } catch (GlobalExceptionHandler.BadRequest ex) {
            throw new GlobalExceptionHandler.BadRequest("QWEQWE");
        }
    }

    private String RandomAddress() {
        String[] addresses = {"123 Main St", "456 Elm St", "789 Oak St", "101 Maple Ave", "222 Pine St", "333 Cedar Rd"};
        Random random = new Random();
        return addresses[random.nextInt(addresses.length)];
    }




    public void deleteById(Long id) {

        log.info("Eliminando pedido con ID: {}", id);

        orderRepository.deleteById(id);
    }
}




