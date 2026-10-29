package com.groceryflow.inventory.repository;

import com.groceryflow.inventory.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<InventoryTransaction, Long> {

    List<InventoryTransaction> findByProductIdOrderByCreatedAtDesc(Long productId);

    List<InventoryTransaction> findByOrderReferenceId(Long orderReferenceId);
}
