package com.example.task_tracker.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.task_tracker.domain.entities.Auth;
import com.example.task_tracker.jwt.JwtService;
import com.example.task_tracker.repositories.AuthRepository;
import com.example.task_tracker.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthRepository authRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public Map<String, String> register(Auth auth) {
        boolean exists = authRepository.findByUserName(auth.getUserName()).isPresent();

        if (exists) {
            throw new IllegalArgumentException("Username already taken!");
        }
        String hashed = passwordEncoder.encode(auth.getPassWord());
        auth.setPassWord(hashed);
        auth.setRole(List.of("ROLE_USER"));
        authRepository.save(auth);
        return Map.of("message", "Register Successfully!");
    }

    @Override
    public Map<String, String> login(Auth auth) {
        Auth existingAuth = authRepository.findByUserName(auth.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password!"));

        if (passwordEncoder.matches(auth.getPassWord(), existingAuth.getPassWord())) {
            String token = jwtService.generateToken(existingAuth.getIdAuth().toString(), existingAuth.getRole());
            return Map.of("accessToken", token);
        }

        throw new IllegalArgumentException("Invalid Username or Password!");
    }

}
