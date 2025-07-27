package com.example.Ecommerce.cart.dto;

import com.example.Ecommerce.product.dto.ProductDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CartItemDTO {
    private Long id;
    private ProductDTO product;
    private int quantity;
}

