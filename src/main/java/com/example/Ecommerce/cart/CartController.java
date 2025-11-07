package com.example.Ecommerce.cart;

import com.example.Ecommerce.appuser.AppUser;
import com.example.Ecommerce.cart.dto.CartDTO;
import com.example.Ecommerce.order.dto.DTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 1. Sepeti getir
    @GetMapping
    public ResponseEntity<CartDTO> getCart(@RequestBody AppUser user) {
        Cart cart = cartService.getCart(user);
        return ResponseEntity.ok(DTOConverter.convertToCartDTO(cart));
    }

    // 2. Sepete ürün ekle
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestBody AddToCartRequest request
    ) {
        cartService.addToCart(request.getUser(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok("Product added to cart");
    }

    // 3. Sepetten ürün sil
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(
            @RequestBody AppUser user,
            @PathVariable Long productId
    ) {
        cartService.removeFromCart(user, productId);
        return ResponseEntity.ok("Product removed from cart");
    }
}
