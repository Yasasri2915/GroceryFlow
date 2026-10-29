package com.groceryflow.inventory;

import com.groceryflow.inventory.entity.InventoryItem;
import com.groceryflow.inventory.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedInitialStock(InventoryRepository inventoryRepository) {
        return args -> {
            if (inventoryRepository.count() == 0) {
                List<InventoryItem> stockList = new ArrayList<>();
                // Seed stock for products 1 to 20
                for (long productId = 1; productId <= 20; productId++) {
                    int initialStock = 50 + (int) (productId * 2);
                    stockList.add(new InventoryItem(productId, initialStock, 15));
                }
                inventoryRepository.saveAll(stockList);
                System.out.println(">>> Seeded initial stock for 20 grocery products into inventory_db successfully.");
            }
        };
    }
}
