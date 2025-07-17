package com.example.Ecommerce.cart;

import com.example.Ecommerce.appuser.AppUser;
import com.example.Ecommerce.product.Product;
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
    public ResponseEntity<Cart> getCart(@RequestBody AppUser user) {
        Cart cart = cartService.getCart(user);
        return ResponseEntity.ok(cart);
    }

    // 2. Sepete ürün ekle
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestBody AddToCartRequest request
    ) {
        cartService.addToCart(request.getUser(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok("Ürün sepete eklendi");
    }

    // 3. Sepetten ürün sil
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(
            @RequestBody AppUser user,
            @PathVariable Long productId
    ) {
        cartService.removeFromCart(user, productId);
        return ResponseEntity.ok("Ürün sepetten silindi");
    }
}
