package com.backend.Ecommerce.order;

import com.backend.Ecommerce.appuser.AppUser;
import com.backend.Ecommerce.appuser.AppUserRepository;
import com.backend.Ecommerce.cart.Cart;
import com.backend.Ecommerce.cart.CartItem;
import com.backend.Ecommerce.cart.CartRepository;
import com.backend.Ecommerce.order.dto.OrderDTO;
import com.backend.Ecommerce.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private OrderProducer orderProducer;
    @InjectMocks
    private OrderService orderService;

    private AppUser user;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new AppUser();
        user.setId(1L);

        product = new Product();
        product.setId(100L);
        product.setProductPrice(new BigDecimal("250.00"));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        cart = new Cart();
        cart.setUser(user);
        cart.setItems(List.of(cartItem));
    }

    @Test
    void testCreateOrder_ThrowsWhenUserIdIsNull() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.createOrder(request)
        );

        assertEquals("userId can not be null", exception.getMessage());
    }


    @Test
    void testCreateOrder_Success() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setProductIdQuantityMap(Map.of(100L, 2));

        AppUser user = new AppUser();
        user.setId(1L);

        Product product = new Product();
        product.setId(100L);
        product.setProductPrice(BigDecimal.valueOf(100));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>(List.of(cartItem)));

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        orderService.createOrder(request);

        assertTrue(cart.getItems().isEmpty());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testCreateOrder_ThrowsWhenUserNotFound() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(2L);

        when(appUserRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> orderService.createOrder(request));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testCreateOrder_ThrowsWhenCartNotFound() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));
        assertEquals("Users cart could not be found", ex.getMessage());
    }

    @Test
    void testCreateOrder_ThrowsWhenCartIsEmpty() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);

        AppUser user = new AppUser();
        user.setId(1L);

        Cart emptyCart = new Cart();
        emptyCart.setUser(user);
        emptyCart.setItems(new ArrayList<>());

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(emptyCart));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> orderService.createOrder(request));

        assertEquals("Cart is empty. Cannot create an order", exception.getMessage());
    }

    @Test
    void testGetOrders_ReturnsUserOrders() {
        AppUser user = new AppUser();
        user.setId(1L);

        Order order1 = new Order();
        order1.setUser(user);

        Order order2 = new Order();
        order2.setUser(user);

        List<Order> mockOrders = List.of(order1, order2);

        when(orderRepository.findAllByUser(user)).thenReturn(mockOrders);

        List<OrderDTO> result = orderService.getOrders(user);

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAllByUser(user);
    }



    @Test
    void testGetOrderById_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setUser(user);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDTO result = orderService.getOrderById(1L, user);

        assertEquals(1L, result.getId());
    }

    @Test
    void testGetOrderById_ThrowsWhenNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> orderService.getOrderById(1L, user));
        assertEquals("Could not find the order", ex.getMessage());
    }

    @Test
    void testGetOrderById_ThrowsWhenUserDoesNotOwnOrder() {
        AppUser otherUser = new AppUser();
        otherUser.setId(99L);

        Order order = new Order();
        order.setId(1L);
        order.setUser(otherUser);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> orderService.getOrderById(1L, user));
        assertEquals("This order does not belong to you", ex.getMessage());
    }
}
