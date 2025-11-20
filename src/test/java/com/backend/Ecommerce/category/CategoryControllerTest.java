package com.backend.Ecommerce.category;

import com.backend.Ecommerce.category.dto.CategoryDTO;
import com.backend.Ecommerce.product.Product;
import com.backend.Ecommerce.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;
    @Mock
    private CategoryService categoryService;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        CategoryDTO cat1 = new CategoryDTO();
        cat1.setId(1L);
        cat1.setName("Cat1");

        CategoryDTO cat2 = new CategoryDTO();
        cat2.setId(2L);
        cat2.setName("Cat2");

        when(categoryService.getAllCategories()).thenReturn(List.of(cat1, cat2));

        List<CategoryDTO> result = categoryController.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Cat1", result.get(0).getName());
    }

    @Test
    void testCreateCategory() {
        CategoryDTO inputDto = new CategoryDTO();
        inputDto.setName("NewCat");

        CategoryDTO returnedDto = new CategoryDTO();
        returnedDto.setId(10L);
        returnedDto.setName("NewCat");

        when(categoryService.createCategory(inputDto)).thenReturn(returnedDto);

        CategoryDTO result = categoryController.createCategory(inputDto);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("NewCat", result.getName());
    }


    @Test
    void testDeleteCategory() {
        doNothing().when(categoryService).deleteCategory(5L);

        categoryController.deleteCategory(5L);

        verify(categoryService, times(1)).deleteCategory(5L);
    }

    @Test
    void testGetProductsByCategory() {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setProductName("Product1");

        Product p2 = new Product();
        p2.setId(2L);
        p2.setProductName("Product2");

        when(productRepository.findByCategoryId(3L)).thenReturn(List.of(p1, p2));

        List<Product> result = categoryController.getProductsByCategory(3L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getProductName());
    }

}

