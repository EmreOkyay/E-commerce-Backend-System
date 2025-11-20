package com.backend.Ecommerce.cart.dto;

import com.backend.Ecommerce.product.dto.ProductDTO;
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

