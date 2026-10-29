package com.groceryflow.auth.controller;

import com.groceryflow.auth.dto.UserDto;
import com.groceryflow.auth.entity.Role;
import com.groceryflow.auth.exception.BadRequestException;
import com.groceryflow.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        if (userId != null) {
            return ResponseEntity.ok(userService.getUserById(userId));
        } else if (userEmail != null && !userEmail.isBlank()) {
            return ResponseEntity.ok(userService.getUserByEmail(userEmail));
        } else {
            throw new BadRequestException("User context headers are missing");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(value = "role", required = false) Role role) {
        if (role != null) {
            return ResponseEntity.ok(userService.getUsersByRole(role));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
