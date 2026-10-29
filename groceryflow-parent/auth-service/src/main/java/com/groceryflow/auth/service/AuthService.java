package com.groceryflow.auth.service;

import com.groceryflow.auth.config.JwtTokenProvider;
import com.groceryflow.auth.dto.AuthResponse;
import com.groceryflow.auth.dto.LoginRequest;
import com.groceryflow.auth.dto.RegisterRequest;
import com.groceryflow.auth.entity.Role;
import com.groceryflow.auth.entity.User;
import com.groceryflow.auth.exception.BadRequestException;
import com.groceryflow.auth.exception.UnauthorizedException;
import com.groceryflow.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new BadRequestException("User with email " + request.getEmail() + " already exists");
        }

        Role role = request.getRole() != null ? request.getRole() : Role.CUSTOMER;

        User user = new User(
                request.getEmail().toLowerCase().trim(),
                passwordEncoder.encode(request.getPassword()),
                request.getFullName().trim(),
                request.getPhone() != null ? request.getPhone().trim() : null,
                role
        );

        User saved = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(saved);

        return new AuthResponse(
                token,
                saved.getId(),
                saved.getEmail(),
                saved.getFullName(),
                saved.getRole(),
                jwtTokenProvider.getExpirationMs()
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!user.isActive()) {
            throw new UnauthorizedException("Account has been deactivated. Please contact support.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateToken(user);

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                jwtTokenProvider.getExpirationMs()
        );
    }
}
