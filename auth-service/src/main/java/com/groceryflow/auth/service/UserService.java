package com.groceryflow.auth.service;

import com.groceryflow.auth.dto.UserDto;
import com.groceryflow.auth.entity.Role;
import com.groceryflow.auth.entity.User;
import com.groceryflow.auth.exception.ResourceNotFoundException;
import com.groceryflow.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserDto.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return UserDto.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }
}
