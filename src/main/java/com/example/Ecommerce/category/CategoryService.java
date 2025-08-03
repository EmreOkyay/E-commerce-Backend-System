package com.example.Ecommerce.category;

import com.example.Ecommerce.category.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    CategoryDTO createCategory(CategoryDTO dto);
    void deleteCategory(Long id);
}


