package com.example.Ecommerce.cart;

import com.example.Ecommerce.product.dto.ProductDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Data
public class CartItemDTO {
    private Long id;
    private ProductDTO product;
    private int quantity;
}

