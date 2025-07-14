package com.example.Ecommerce.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public ArrayList<Product> findAllProducts() {
        return productRepository.findAllProducts();
    }

    public Optional<Product> findProductByName(String productName) {
        return productRepository.findProductByName(productName);
    }

    public ArrayList<Product> getProductsWithAvailableStock() {
        return productRepository.findProductsWithAvailableStock();
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }


    @Transactional
    public void buyProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        if (product.getProductStock() <= 0) {
            throw new IllegalStateException("No stock left");
        }

        product.setProductStock(product.getProductStock() - 1);

        if (product.getProductStock() == 0) {
            product.setIsAvailable(false);
        }

        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
    }
}

