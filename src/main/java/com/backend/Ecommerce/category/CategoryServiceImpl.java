package com.backend.Ecommerce.category;

import com.backend.Ecommerce.category.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> {
                    CategoryDTO dto = new CategoryDTO();
                    dto.setId(c.getId());
                    dto.setName(c.getName());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        Category saved = categoryRepository.save(category);
        dto.setId(saved.getId());
        return dto;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}

// TODO: create relation between category and product [one-to-many]

