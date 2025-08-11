package com.example.Ecommerce.product;

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
        return productService.getProductsWithAvailableStock();
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

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Optional<Product> existingProduct = productService.findProductById(id);

        if (existingProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        existingProduct.get().setProductName(updatedProduct.getProductName());
        existingProduct.get().setProductDescription(updatedProduct.getProductDescription());
        existingProduct.get().setProductPrice(updatedProduct.getProductPrice());
        existingProduct.get().setProductCategory(updatedProduct.getProductCategory());
        existingProduct.get().setProductStock(updatedProduct.getProductStock());

        Product savedProduct = productService.saveProduct(existingProduct.orElse(null));

        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
