package com.groceryflow.product;

import com.groceryflow.product.entity.Category;
import com.groceryflow.product.entity.Product;
import com.groceryflow.product.repository.CategoryRepository;
import com.groceryflow.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedGroceryCatalog(CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                // Categories
                Category vegetables = categoryRepository.save(new Category("Vegetables", "vegetables", "Farm fresh organic vegetables", "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=400&q=80"));
                Category fruits = categoryRepository.save(new Category("Fruits", "fruits", "Juicy and ripe seasonal fruits", "https://images.unsplash.com/photo-1619566636858-adf3ef46400b?auto=format&fit=crop&w=400&q=80"));
                Category dairy = categoryRepository.save(new Category("Dairy & Eggs", "dairy-eggs", "Fresh milk, cheese, butter, and farm eggs", "https://images.unsplash.com/photo-1528750997573-59b89d56f4f7?auto=format&fit=crop&w=400&q=80"));
                Category bakery = categoryRepository.save(new Category("Bakery", "bakery", "Artisan bread, buns, and morning bakes", "https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=400&q=80"));
                Category beverages = categoryRepository.save(new Category("Beverages", "beverages", "Refreshing juices, teas, and cold brew coffees", "https://images.unsplash.com/photo-1527661591475-527312dd65f5?auto=format&fit=crop&w=400&q=80"));
                Category pantry = categoryRepository.save(new Category("Pantry Staples", "pantry", "Rice, grains, pulses, and cooking essentials", "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=400&q=80"));

                // Products
                List<Product> products = List.of(
                        new Product(vegetables.getId(), "Organic Red Tomatoes", "Fresh vine-ripened tomatoes rich in antioxidants", new BigDecimal("45.00"), "1 kg", "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?auto=format&fit=crop&w=400&q=80", true),
                        new Product(vegetables.getId(), "Fresh Spinach (Palak)", "Tender, iron-rich green leaves harvested daily", new BigDecimal("30.00"), "500 g", "https://images.unsplash.com/photo-1576045057995-568f588f82fb?auto=format&fit=crop&w=400&q=80", true),
                        new Product(vegetables.getId(), "English Cucumbers", "Crisp and hydrating garden cucumbers", new BigDecimal("35.00"), "500 g", "https://images.unsplash.com/photo-1449300079323-02e209d9d3a6?auto=format&fit=crop&w=400&q=80", false),
                        new Product(vegetables.getId(), "Baby Potatoes", "Perfect golden skin potatoes for roasting and curry", new BigDecimal("40.00"), "1 kg", "https://images.unsplash.com/photo-1518977676601-b53f82aba655?auto=format&fit=crop&w=400&q=80", false),
                        new Product(vegetables.getId(), "Sweet Bell Peppers (Trio)", "Red, yellow, and green crunchy capsicums", new BigDecimal("85.00"), "Pack of 3", "https://images.unsplash.com/photo-1563565375-f3fdfdbefa83?auto=format&fit=crop&w=400&q=80", true),

                        new Product(fruits.getId(), "Shimla Royal Apples", "Sweet, aromatic, and crispy mountain apples", new BigDecimal("180.00"), "1 kg", "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?auto=format&fit=crop&w=400&q=80", true),
                        new Product(fruits.getId(), "Robusta Bananas", "Naturally ripened high-potassium bananas", new BigDecimal("55.00"), "1 Dozen", "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?auto=format&fit=crop&w=400&q=80", true),
                        new Product(fruits.getId(), "Sweet Valencia Oranges", "Juicy citrus packed with natural Vitamin C", new BigDecimal("120.00"), "1 kg", "https://images.unsplash.com/photo-1547514701-42782101795e?auto=format&fit=crop&w=400&q=80", false),
                        new Product(fruits.getId(), "Green Seedless Grapes", "Plump, sweet and tangy table grapes", new BigDecimal("95.00"), "500 g", "https://images.unsplash.com/photo-1537640538966-79f369143f8f?auto=format&fit=crop&w=400&q=80", false),

                        new Product(dairy.getId(), "Organic Farm Fresh Milk", "Pasteurized whole cow milk, homogenised", new BigDecimal("68.00"), "1 L", "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=400&q=80", true),
                        new Product(dairy.getId(), "Free-Range Brown Eggs", "Protein-packed farm fresh certified eggs", new BigDecimal("90.00"), "Pack of 6", "https://images.unsplash.com/photo-1582722872445-44dc5f7e3c8f?auto=format&fit=crop&w=400&q=80", true),
                        new Product(dairy.getId(), "Pure Malai Paneer", "Soft and fresh cottage cheese blocks", new BigDecimal("110.00"), "200 g", "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?auto=format&fit=crop&w=400&q=80", false),
                        new Product(dairy.getId(), "Unsalted Creamery Butter", "Rich artisanal churned butter", new BigDecimal("135.00"), "250 g", "https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?auto=format&fit=crop&w=400&q=80", false),

                        new Product(bakery.getId(), "Whole Wheat Sourdough Bread", "Naturally fermented slow baked loaf", new BigDecimal("80.00"), "400 g", "https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=400&q=80", true),
                        new Product(bakery.getId(), "Multigrain Burger Buns", "Soft baked buns loaded with roasted sesame and flax", new BigDecimal("50.00"), "Pack of 4", "https://images.unsplash.com/photo-1586190848861-99aa4a171e90?auto=format&fit=crop&w=400&q=80", false),

                        new Product(beverages.getId(), "Cold-Pressed Valencia Orange Juice", "100% pure juice with zero added sugar or preservatives", new BigDecimal("125.00"), "500 ml", "https://images.unsplash.com/photo-1621506289937-a8e4df240d0b?auto=format&fit=crop&w=400&q=80", true),
                        new Product(beverages.getId(), "Assam Premium CTC Tea", "Strong and invigorating morning tea blend", new BigDecimal("160.00"), "500 g", "https://images.unsplash.com/photo-1576092768241-dec231879fc3?auto=format&fit=crop&w=400&q=80", false),

                        new Product(pantry.getId(), "Royal Basmati Rice (Aged 2 Yrs)", "Long grain fragrant basmati rice", new BigDecimal("220.00"), "1 kg", "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=400&q=80", true),
                        new Product(pantry.getId(), "Organic Toor Dal", "Unpolished protein-dense yellow lentils", new BigDecimal("140.00"), "1 kg", "https://images.unsplash.com/photo-1515543237350-b3eea1ec8082?auto=format&fit=crop&w=400&q=80", false),
                        new Product(pantry.getId(), "Cold Pressed Mustard Oil", "Traditional kachi ghani pure cooking oil", new BigDecimal("190.00"), "1 L", "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?auto=format&fit=crop&w=400&q=80", false)
                );

                productRepository.saveAll(products);
                System.out.println(">>> Seeded 6 Categories and 20 Products into product_db successfully.");
            }
        };
    }
}
