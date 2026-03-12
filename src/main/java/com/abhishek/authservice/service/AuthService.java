package com.abhishek.authservice.service;

import com.abhishek.authservice.dto.AuthResponse;
import com.abhishek.authservice.dto.LoginRequest;
import com.abhishek.authservice.dto.RegisterRequest;
import com.abhishek.authservice.entity.Role;
import com.abhishek.authservice.entity.User;
import com.abhishek.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Register new user
    public AuthResponse register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Build user entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        // Save to database
        userRepository.save(user);

        // Generate JWT token
            String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities(user.getRole().name()) // ✅ changed from .roles()
                        .build()
        );

        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().name());
    }

    // Login existing user
    public AuthResponse login(LoginRequest request) {

        // This line does everything — checks email exists + password matches
        // Throws exception automatically if credentials are wrong
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // If we reach here, credentials are valid
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token
      // Generate JWT token
String token = jwtService.generateToken(
    org.springframework.security.core.userdetails.User
            .withUsername(user.getEmail())
            .password(user.getPassword())
            .authorities(user.getRole().name()) // ✅ changed from .roles()
            .build()
);

        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().name());
    }
}