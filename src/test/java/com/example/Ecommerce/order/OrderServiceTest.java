package com.example.Ecommerce.order;

import com.example.Ecommerce.appuser.AppUser;
import com.example.Ecommerce.appuser.AppUserRepository;
import com.example.Ecommerce.cart.Cart;
import com.example.Ecommerce.cart.CartItem;
import com.example.Ecommerce.cart.CartRepository;
import com.example.Ecommerce.product.Product;
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

        assertEquals("userId null olamaz.", exception.getMessage());
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
        assertEquals("Kullanıcının sepeti bulunamadı", ex.getMessage());
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

        assertEquals("Sepet boş. Sipariş oluşturulamaz.", exception.getMessage());
    }

    @Test
    void testGetOrders_ReturnsUserOrders() {
        AppUser user = new AppUser();
        user.setId(1L);

        List<Order> mockOrders = List.of(new Order(), new Order());

        when(orderRepository.findAllByUser(user)).thenReturn(mockOrders);

        List<Order> result = orderService.getOrders(user);

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAllByUser(user);
    }



    @Test
    void testGetOrderById_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setUser(user);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L, user);

        assertEquals(1L, result.getId());
    }

    @Test
    void testGetOrderById_ThrowsWhenNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> orderService.getOrderById(1L, user));
        assertEquals("Sipariş bulunamadı", ex.getMessage());
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
        assertEquals("Bu sipariş size ait değil.", ex.getMessage());
    }
}
