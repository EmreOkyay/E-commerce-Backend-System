package com.backend.Ecommerce.category;

import com.backend.Ecommerce.category.dto.CategoryDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void testGetAllCategories() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("Cat1");

        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("Cat2");

        when(categoryRepository.findAll()).thenReturn(List.of(cat1, cat2));

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Cat1", result.get(0).getName());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testCreateCategory() {
        CategoryDTO inputDto = new CategoryDTO();
        inputDto.setName("NewCat");

        Category savedCategory = new Category();
        savedCategory.setId(10L);
        savedCategory.setName("NewCat");

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryDTO result = categoryService.createCategory(inputDto);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("NewCat", result.getName());
    }

    @Test
    void testDeleteCategory() {
        Long categoryId = 5L;

        doNothing().when(categoryRepository).deleteById(categoryId);

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}


