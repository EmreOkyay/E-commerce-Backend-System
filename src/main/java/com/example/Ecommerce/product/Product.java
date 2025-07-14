package com.example.Ecommerce.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {

    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private String productCategory;
    private Long productStock;
    private Boolean isAvailable = true;

    public Product(String productName, String productDescription, BigDecimal productPrice, String productCategory, Long productStock, Boolean isAvailable) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productStock = productStock;
        this.isAvailable = isAvailable;
    }

    public Product(String productName, BigDecimal productPrice, Long productStock, String productCategory,  Boolean isAvailable) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productCategory = productCategory;
        this.isAvailable = isAvailable;
    }
}
