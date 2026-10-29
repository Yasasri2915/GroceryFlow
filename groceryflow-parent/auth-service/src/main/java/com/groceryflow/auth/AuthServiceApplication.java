package com.groceryflow.auth;

import com.groceryflow.auth.entity.Role;
import com.groceryflow.auth.entity.User;
import com.groceryflow.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedDemoUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                // 1. Admin
                User admin = new User(
                        "admin@groceryflow.com",
                        passwordEncoder.encode("admin123"),
                        "System Administrator",
                        "+91 9876543210",
                        Role.ADMIN
                );
                userRepository.save(admin);

                // 2. Warehouse Staff
                User staff = new User(
                        "staff@groceryflow.com",
                        passwordEncoder.encode("staff123"),
                        "Warehouse Staff Lead",
                        "+91 9876543211",
                        Role.STAFF
                );
                userRepository.save(staff);

                // 3. Customer
                User customer = new User(
                        "customer@groceryflow.com",
                        passwordEncoder.encode("customer123"),
                        "Demo Customer",
                        "+91 9876543212",
                        Role.CUSTOMER
                );
                userRepository.save(customer);

                System.out.println(">>> Demo users seeded successfully (Admin, Staff, Customer).");
            }
        };
    }
}
