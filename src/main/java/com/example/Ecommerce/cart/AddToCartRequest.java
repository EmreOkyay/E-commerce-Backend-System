package com.example.Ecommerce.cart;

import com.example.Ecommerce.appuser.AppUser;
import lombok.Data;

@Data
public class AddToCartRequest {
    private AppUser user;
    private Long productId;
    private int quantity;
}
