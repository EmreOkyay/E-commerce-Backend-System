package com.backend.Ecommerce.cart;

import com.backend.Ecommerce.appuser.AppUser;
import com.backend.Ecommerce.product.Product;
import com.backend.Ecommerce.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    private AppUser user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        product = new Product();
        product.setId(1L);
    }

    @Test
    void itShouldCreateNewCartIfNotExists() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart cart = cartService.getCart(user);

        assertEquals(user, cart.getUser());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void itShouldReturnExistingCart() {
        Cart existingCart = new Cart();
        existingCart.setUser(user);

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(existingCart));

        Cart cart = cartService.getCart(user);

        assertEquals(user, cart.getUser());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void itShouldAddNewItemToCart() {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        cartService.addToCart(user, 1L, 3);

        assertEquals(1, cart.getItems().size());
        CartItem addedItem = cart.getItems().get(0);
        assertEquals(product, addedItem.getProduct());
        assertEquals(3, addedItem.getQuantity());

        verify(cartRepository).save(cart);
    }

    @Test
    void itShouldIncreaseQuantityIfItemAlreadyInCart() {
        CartItem existingItem = new CartItem();
        existingItem.setProduct(product);
        existingItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>(List.of(existingItem)));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        cartService.addToCart(user, 1L, 5);

        assertEquals(1, cart.getItems().size());
        assertEquals(7, existingItem.getQuantity());

        verify(cartRepository).save(cart);
    }

    @Test
    void itShouldRemoveItemFromCart() {
        CartItem item = new CartItem();
        Product otherProduct = new Product();
        otherProduct.setId(2L);

        item.setProduct(product);
        CartItem otherItem = new CartItem();
        otherItem.setProduct(otherProduct);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>(List.of(item, otherItem)));

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        cartService.removeFromCart(user, 1L);

        assertEquals(1, cart.getItems().size());
        assertEquals(2L, cart.getItems().get(0).getProduct().getId());

        verify(cartRepository).save(cart);
    }

    @Test
    void itShouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cartService.addToCart(user, 1L, 2));

        assertEquals("Product not found", exception.getMessage());
    }
}
