package com.example.Ecommerce.product;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.Ecommerce.redis.CacheUpdatePublisher;
import com.example.Ecommerce.redis.RedisCacheHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    RedisCacheHelper redisCacheHelper;

    @Mock
    CacheUpdatePublisher cacheUpdatePublisher;

    @InjectMocks
    ProductService productService;

    @Test
    void itShouldSaveProduct() {
        Product product = new Product();
        product.setProductName("Test Product");

        when(productRepository.save(product)).thenReturn(product);

        Product saved = productService.saveProduct(product);

        assertEquals(product, saved);
        verify(productRepository).save(product);
    }

    @Test
    void itShouldFindProductById() {
        Product product = new Product();
        product.setProductName("Test");

        when(redisCacheHelper.getOrLoadOptional(eq("product_1"),any(), any())).thenReturn(Optional.of(product));

        Optional<Product> found = productService.findProductById(1L);

        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getProductName());
        verify(redisCacheHelper).getOrLoadOptional(eq("product_1"), any(), any());
    }

    @Test
    void itShouldFindAllProducts() {
        Product p1 = new Product();
        p1.setProductName("P1");
        Product p2 = new Product();
        p2.setProductName("P2");

        ArrayList<Product> mockList = new ArrayList<>(List.of(p1, p2));

        when(redisCacheHelper.getOrLoad(eq("all_products"), any(com.fasterxml.jackson.core.type.TypeReference.class), any())).thenReturn(mockList);

        ArrayList<Product> products = productService.findAllProducts();

        assertEquals(2, products.size());
        assertEquals("P1", products.get(0).getProductName());
        verify(redisCacheHelper).getOrLoad(eq("all_products"), any(), any());
    }

    @Test
    void itShouldFindProductByName() {
        Product product = new Product();
        product.setProductName("Name");

        when(redisCacheHelper.getOrLoadOptional(eq("product_Name"), any(), any())).thenReturn(Optional.of(product));

        Optional<Product> found = productService.findProductByName("Name");

        assertTrue(found.isPresent());
        assertEquals("Name", found.get().getProductName());

        verify(redisCacheHelper).getOrLoadOptional(eq("product_Name"), any(), any());
    }

    @Test
    void itShouldGetProductsWithAvailableStock() {
        Product p1 = new Product();
        p1.setProductStock(5L);
        Product p2 = new Product();
        p2.setProductStock(10L);

        ArrayList<Product> mockList = new ArrayList<>(List.of(p1, p2));

        when(redisCacheHelper.getOrLoad(eq("available_products"), any(com.fasterxml.jackson.core.type.TypeReference.class), any())).thenReturn(mockList);

        ArrayList<Product> available = productService.getProductsWithAvailableStock();

        assertFalse(available.isEmpty());
        verify(redisCacheHelper).getOrLoad(eq("available_products"), any(), any());
    }

    @Test
    void itShouldAddProduct() {
        Product product = new Product();
        product.setProductName("New Product");

        when(productRepository.save(product)).thenReturn(product);

        productService.addProduct(product);

        verify(productRepository).save(product);
    }

    @Test
    void itShouldBuyProductSuccessfully() {
        Product product = new Product();
        product.setProductStock(2L);
        product.setIsAvailable(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.buyProduct(1L);

        assertEquals(1, product.getProductStock());
        assertTrue(product.getIsAvailable());
        verify(productRepository).save(product);
    }

    @Test
    void itShouldThrowWhenBuyingProductWithNoStock() {
        Product product = new Product();
        product.setProductStock(0L);
        product.setIsAvailable(false);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> productService.buyProduct(1L));

        assertEquals("No stock left", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void itShouldThrowWhenBuyingNonExistentProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> productService.buyProduct(1L));

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void itShouldDeleteProductSuccessfully() {
        when(productRepository.existsById(1L)).thenReturn(true);

        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void itShouldThrowWhenDeletingNonExistentProduct() {
        when(productRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.deleteProduct(1L));

        assertEquals("Product with id 1 not found", exception.getMessage());
        verify(productRepository, never()).deleteById(any());
    }
}
