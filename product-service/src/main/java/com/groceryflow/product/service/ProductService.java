package com.groceryflow.product.service;

import com.groceryflow.product.dto.ProductDto;
import com.groceryflow.product.dto.ProductRequest;
import com.groceryflow.product.entity.Category;
import com.groceryflow.product.entity.Product;
import com.groceryflow.product.exception.ResourceNotFoundException;
import com.groceryflow.product.repository.CategoryRepository;
import com.groceryflow.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    private Map<Long, String> getCategoryNameMap() {
        return categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName, (existing, replacement) -> existing));
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        Map<Long, String> categoryNames = getCategoryNameMap();
        return productRepository.findByActiveTrue().stream()
                .map(p -> ProductDto.fromEntity(p, categoryNames.getOrDefault(p.getCategoryId(), "General")))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        String categoryName = categoryRepository.findById(product.getCategoryId())
                .map(Category::getName)
                .orElse("General");
        return ProductDto.fromEntity(product, categoryName);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        Map<Long, String> categoryNames = getCategoryNameMap();
        return productRepository.findByCategoryIdAndActiveTrue(categoryId).stream()
                .map(p -> ProductDto.fromEntity(p, categoryNames.getOrDefault(p.getCategoryId(), "General")))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getFeaturedProducts() {
        Map<Long, String> categoryNames = getCategoryNameMap();
        return productRepository.findByFeaturedTrueAndActiveTrue().stream()
                .map(p -> ProductDto.fromEntity(p, categoryNames.getOrDefault(p.getCategoryId(), "General")))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDto> searchProducts(String query) {
        Map<Long, String> categoryNames = getCategoryNameMap();
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(query).stream()
                .map(p -> ProductDto.fromEntity(p, categoryNames.getOrDefault(p.getCategoryId(), "General")))
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDto createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = new Product(
                category.getId(),
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getUnit(),
                request.getImageUrl(),
                request.isFeatured()
        );

        Product saved = productRepository.save(product);
        return ProductDto.fromEntity(saved, category.getName());
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        product.setCategoryId(category.getId());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setUnit(request.getUnit());
        product.setImageUrl(request.getImageUrl());
        product.setFeatured(request.isFeatured());

        Product updated = productRepository.save(product);
        return ProductDto.fromEntity(updated, category.getName());
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setActive(false);
        productRepository.save(product);
    }
}
