package com.backend.Ecommerce.cart;

import com.backend.Ecommerce.appuser.AppUser;
import com.backend.Ecommerce.cart.dto.CartDTO;
import com.backend.Ecommerce.order.dto.DTOConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        cart.setId(1L);
        cart.setUser(user);

        CartDTO cartDTO = DTOConverter.convertToCartDTO(cart);

        when(cartService.getCart(user)).thenReturn(cart);

        // when
        CartDTO result = cartController.getCart(user).getBody();

        // then
        assertNotNull(result);
        assertEquals(cartDTO.getId(), result.getId());
        assertEquals(cartDTO.getUser().getEmail(), result.getUser().getEmail());
    }


    @Test
    void itShouldAddToCart() {
        // given
        AppUser user = new AppUser();
        AddToCartRequest request = new AddToCartRequest(user, 1L, 2);
        String expectedMessage = "Product added to cart";

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
        String expectedMessage = "Product removed from cart";

        // when
        ResponseEntity<String> response = cartController.removeFromCart(user, productId);

        // then
        assertEquals(expectedMessage, response.getBody());
        verify(cartService, times(1)).removeFromCart(user, productId);
    }
}
