package com.groceryflow.product.service;

import com.groceryflow.product.dto.CategoryDto;
import com.groceryflow.product.dto.CategoryRequest;
import com.groceryflow.product.entity.Category;
import com.groceryflow.product.exception.ResourceNotFoundException;
import com.groceryflow.product.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findByActiveTrue().stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return CategoryDto.fromEntity(category);
    }

    @Transactional
    public CategoryDto createCategory(CategoryRequest request) {
        Category category = new Category(
                request.getName(),
                request.getSlug().toLowerCase().trim(),
                request.getDescription(),
                request.getImageUrl()
        );
        Category saved = categoryRepository.save(category);
        return CategoryDto.fromEntity(saved);
    }
}
