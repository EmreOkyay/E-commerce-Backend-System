package com.example.Login.product;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping(path = "/{productName}")
    public Optional<Product> findProductByName(@PathVariable("productName") String productName) {
        return productService.findProductByName(productName);
    }

    @GetMapping(path = "/available")
    public ArrayList<Product> findProductsWithAvailableStock() {
        return productService.findProductsWithAvailableStock();
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PostMapping("/buy/{id}")
    public void buyProduct(@PathVariable Long id) {
        productService.buyProduct(id);
    }
}
