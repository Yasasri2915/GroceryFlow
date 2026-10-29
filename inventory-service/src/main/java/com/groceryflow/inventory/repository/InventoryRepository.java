package com.groceryflow.inventory.repository;

import com.groceryflow.inventory.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findByProductId(Long productId);

    List<InventoryItem> findByProductIdIn(List<Long> productIds);

    @Query("SELECT i FROM InventoryItem i WHERE i.availableQuantity <= i.lowStockThreshold")
    List<InventoryItem> findLowStockItems();
}
