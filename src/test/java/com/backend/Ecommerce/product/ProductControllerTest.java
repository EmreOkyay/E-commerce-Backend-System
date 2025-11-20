package com.backend.Ecommerce.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setProductName("Test Product");
        product.setProductStock(10L);
    }

    @Test
    void itShouldGetAvailableStockProducts() {
        // given
        Product product = new Product();
        product.setProductName("Stocked Product");
        product.setProductDescription("Available product");
        product.setProductPrice(BigDecimal.valueOf(150.0));
        product.setProductCategory("Stock");
        product.setProductStock(10L);

        ArrayList<Product> available = new ArrayList<>(List.of(product));

        when(productService.getProductsWithAvailableStock()).thenReturn(available);

        // when
        ArrayList<Product> result = productController.findProductsWithAvailableStock();

        // then
        assertEquals(1, result.size());
        assertEquals("Stocked Product", result.get(0).getProductName());
        assertTrue(result.get(0).getProductStock() > 0);
    }

    @Test
    void itShouldGetAllProducts() {
        // given
        Product product1 = new Product();
        product1.setProductName("Laptop");

        Product product2 = new Product();
        product2.setProductName("Phone");

        ArrayList<Product> mockProductList = new ArrayList<>();
        mockProductList.add(product1);
        mockProductList.add(product2);

        when(productService.findAllProducts()).thenReturn(mockProductList);

        // when
        List<Product> result = productController.getAllProducts();

        // then
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getProductName());
        assertEquals("Phone", result.get(1).getProductName());
    }



    @Test
    void itShouldFindProductByName() {
        when(productService.findProductByName("Test Product")).thenReturn(Optional.of(product));

        Optional<Product> result = productController.findProductByName("Test Product");

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getProductName());
    }

    @Test
    void itShouldFindProductsWithAvailableStock() {
        ArrayList<Product> available = new ArrayList<>(List.of(product));
        when(productService.getProductsWithAvailableStock()).thenReturn(available);

        ArrayList<Product> result = productController.findProductsWithAvailableStock();

        assertEquals(1, result.size());
    }

    @Test
    void itShouldAddProduct() {
        ResponseEntity<Product> response = productController.addProduct(product);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService).addProduct(product);
    }

    @Test
    void itShouldBuyProduct() {
        // given
        Long productId = 1L;

        // when
        productController.buyProduct(productId);

        // then
        verify(productService, times(1)).buyProduct(productId);
    }


    @Test
    void itShouldUpdateProductIfExists() {
        Product updated = new Product();
        updated.setProductName("Updated Name");
        updated.setProductDescription("Updated Desc");
        updated.setProductPrice(BigDecimal.valueOf(100.0));
        updated.setProductCategory("Updated Cat");
        updated.setProductStock(5L);

        when(productService.findProductById(1L)).thenReturn(Optional.of(product));
        when(productService.saveProduct(any(Product.class))).thenReturn(updated);

        ResponseEntity<Product> response = productController.updateProduct(1L, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", response.getBody().getProductName());
    }

    @Test
    void itShouldReturnNotFoundWhenProductToUpdateDoesNotExist() {
        Product updated = new Product();
        updated.setProductName("Doesn't Matter");

        when(productService.findProductById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.updateProduct(99L, updated);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void itShouldDeleteProductSuccessfully() {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<?> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product deleted successfully", response.getBody());
    }

    @Test
    void itShouldHandleDeleteProductFailure() {
        doThrow(new RuntimeException("Product not found")).when(productService).deleteProduct(99L);

        ResponseEntity<?> response = productController.deleteProduct(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
    }
}
