package com.groceryflow.product.dto;

import com.groceryflow.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDto {

    private Long id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String description;
    private BigDecimal price;
    private String unit;
    private String imageUrl;
    private boolean featured;
    private boolean active;
    private LocalDateTime createdAt;

    public ProductDto() {
    }

    public static ProductDto fromEntity(Product product, String categoryName) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setCategoryId(product.getCategoryId());
        dto.setCategoryName(categoryName);
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setUnit(product.getUnit());
        dto.setImageUrl(product.getImageUrl());
        dto.setFeatured(product.isFeatured());
        dto.setActive(product.isActive());
        dto.setCreatedAt(product.getCreatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
