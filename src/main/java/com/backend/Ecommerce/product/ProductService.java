package com.backend.Ecommerce.product;

import com.backend.Ecommerce.redis.CacheUpdatePublisher;
import com.backend.Ecommerce.redis.RedisCacheHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RedisCacheHelper redisCacheHelper;
    private final CacheUpdatePublisher cachePublisher;

    public ProductService(ProductRepository productRepository, RedisCacheHelper redisCacheHelper, CacheUpdatePublisher cachePublisher) {
        this.productRepository = productRepository;
        this.redisCacheHelper = redisCacheHelper;
        this.cachePublisher = cachePublisher;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> findProductById(Long id) {
        return redisCacheHelper.getOrLoadOptional("product_" + id,new TypeReference<Product>() {},() -> productRepository.findById(id));
    }

    public ArrayList<Product> findAllProducts() {
        return redisCacheHelper.getOrLoad("all_products", new TypeReference<ArrayList<Product>>() {}, productRepository::findAllProducts);
    }

    public Optional<Product> findProductByName(String productName) {
        return redisCacheHelper.getOrLoadOptional("product_" + productName,new TypeReference<Product>() {},() -> productRepository.findProductByName(productName));
    }

    public ArrayList<Product> getProductsWithAvailableStock() {
        return redisCacheHelper.getOrLoad("available_products", new TypeReference<ArrayList<Product>>() {}, productRepository::findProductsWithAvailableStock);
    }

    public void addProduct(Product product) {
        productRepository.save(product);
        cachePublisher.publishAddProductEvent(product);
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
        cachePublisher.publishBuyProductEvent(productId);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product with id " + id + " not found");
        }

        productRepository.deleteById(id);
        cachePublisher.publishDeleteProductEvent(id);
    }
}

