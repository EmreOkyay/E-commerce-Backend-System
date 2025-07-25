package com.example.Ecommerce.cart;

import com.example.Ecommerce.appuser.AppUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @Test
    void itShouldGetCart() {
        // given
        AppUser user = new AppUser();
        Cart cart = new Cart();
        when(cartService.getCart(user)).thenReturn(cart);

        // when
        Cart result = cartController.getCart(user).getBody();

        // then
        assertEquals(cart, result);
    }

    @Test
    void itShouldAddToCart() {
        // given
        AppUser user = new AppUser();
        AddToCartRequest request = new AddToCartRequest(user, 1L, 2);
        String expectedMessage = "Ürün sepete eklendi";

        // when
        ResponseEntity<String> response = cartController.addToCart(request);

        // then
        assertEquals(expectedMessage, response.getBody());
        verify(cartService, times(1)).addToCart(user, 1L, 2);
    }

    @Test
    void itShouldRemoveFromCart() {
        // given
        AppUser user = new AppUser();
        long productId = 1L;
        String expectedMessage = "Ürün sepetten silindi";

        // when
        ResponseEntity<String> response = cartController.removeFromCart(user, productId);

        // then
        assertEquals(expectedMessage, response.getBody());
        verify(cartService, times(1)).removeFromCart(user, productId);
    }
}
