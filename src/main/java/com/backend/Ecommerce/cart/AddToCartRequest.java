package com.backend.Ecommerce.cart;

import com.backend.Ecommerce.appuser.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddToCartRequest {
    private AppUser user;
    private Long productId;
    private int quantity;
}
