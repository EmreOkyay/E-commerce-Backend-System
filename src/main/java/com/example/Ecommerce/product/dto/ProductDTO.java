package com.example.Ecommerce.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private String productCategory;
}

