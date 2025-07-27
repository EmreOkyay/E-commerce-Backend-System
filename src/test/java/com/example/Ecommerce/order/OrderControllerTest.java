package com.example.Ecommerce.order;

import com.example.Ecommerce.appuser.AppUser;
import com.example.Ecommerce.appuser.AppUserRepository;
import com.example.Ecommerce.order.dto.OrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerUnitTest {

    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private AppUserRepository appUserRepository;

    private AppUser user;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new AppUser();
        user.setId(1L);
        user.setEmail("atilla@hotmail.com");
        user.setFirstName("Atilla");

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("500.00"));
        order.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testGetOrders() {
        when(appUserRepository.findByEmail("atilla@hotmail.com")).thenReturn(Optional.of(user));

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setStatus(String.valueOf(order.getStatus()));
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setCreatedAt(order.getCreatedAt());

        when(orderService.getOrders(user)).thenReturn(List.of(orderDTO));

        ResponseEntity<List<OrderDTO>> response = orderController.getOrders("atilla@hotmail.com");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(order.getId(), response.getBody().get(0).getId());
    }

    @Test
    void testGetOrderById() {
        when(appUserRepository.findByEmail("atilla@hotmail.com")).thenReturn(Optional.of(user));

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setStatus(String.valueOf(order.getStatus()));
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setCreatedAt(order.getCreatedAt());

        when(orderService.getOrderById(eq(1L), eq(user))).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.getOrderById(1L, "atilla@hotmail.com");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(order.getId(), response.getBody().getId());
    }

    @Test
    void testCreateOrder() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setProductIdQuantityMap(Map.of(10L, 2, 20L, 1));

        ResponseEntity<String> response = orderController.createOrder(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Sipariş başarıyla oluşturuldu.", response.getBody());
        verify(orderService, times(1)).createOrder(request);
    }
}
