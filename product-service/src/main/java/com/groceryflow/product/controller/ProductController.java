package com.groceryflow.product.controller;

import com.groceryflow.product.dto.ProductDto;
import com.groceryflow.product.dto.ProductRequest;
import com.groceryflow.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "featured", required = false) Boolean featured) {

        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(productService.searchProducts(search.trim()));
        }
        if (categoryId != null) {
            return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
        }
        if (Boolean.TRUE.equals(featured)) {
            return ResponseEntity.ok(productService.getFeaturedProducts());
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductDto created = productService.createProduct(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
